package ru.voxp.android.presentation.law.last

import android.os.AsyncTask
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.paging.PagedList
import androidx.paging.PositionalDataSource
import io.reactivex.disposables.Disposable
import ru.jewelline.mvvm.base.presentation.AbstractViewModel
import ru.jewelline.mvvm.base.presentation.ViewModelFactory
import ru.jewelline.mvvm.interfaces.domain.UseCaseOutput.Status.*
import ru.jewelline.mvvm.interfaces.presentation.ViewModel
import ru.voxp.android.domain.api.ExceptionType.CONNECTION
import ru.voxp.android.domain.api.ExceptionType.SERVER
import ru.voxp.android.domain.api.VoxpException
import ru.voxp.android.domain.api.model.Law
import ru.voxp.android.domain.usecase.FetchLawsNetworkAwareUseCase
import ru.voxp.android.domain.usecase.FetchLawsUseCase
import ru.voxp.android.domain.usecase.FetchLawsUseCaseInput
import ru.voxp.android.domain.usecase.FetchLawsUseCaseOutput
import ru.voxp.android.presentation.core.recycler.ViewModelByStateProvider
import ru.voxp.android.presentation.error.ErrorPanelState
import ru.voxp.android.presentation.error.ErrorPanelViewModel
import ru.voxp.android.presentation.law.card.LawCardState
import ru.voxp.android.presentation.law.card.LawCardViewModel
import javax.inject.Inject
import javax.inject.Provider

class LastLawsViewModel @Inject constructor(
    private val fetchLawsUseCase: FetchLawsUseCase,
    private val fetchLawsNetworkAwareUseCase: FetchLawsNetworkAwareUseCase,
    viewModelFactory: ViewModelFactory
) : AbstractViewModel<LastLawsState>(), ErrorPanelViewModel {

    val viewModelByStateProvider: ViewModelByStateProvider
    private var fetchLastLawsTask: Disposable? = null

    init {
        viewModelByStateProvider = ViewModelByStateProvider(viewModelFactory)
            .registerMapping(LawCardState::class, LawCardViewModel::class)
            .registerMapping(ErrorPanelState::class, Provider { this as ViewModel<ErrorPanelState>})
        requestLastLaws()
    }

    override fun buildInitialState(): LastLawsState {
        return LastLawsState()
    }

    private fun requestLastLaws() {
        cancelFetchLastLawsTask()
        fetchLastLawsTask = collectDisposable(
            fetchLawsNetworkAwareUseCase.execute(FetchLawsUseCaseInput(0, 20))
                .subscribe { result ->
                    when (result.status) {
                        IN_PROGRESS -> sendState(LastLawsState.loading(result.connectionAvailable))
                        SUCCESS -> sendState(
                            LastLawsState.laws(
                                mapLawsToPagedList(
                                    result.laws,
                                    result.total
                                )
                            )
                        )
                        FAILURE -> handleGetLastLawsFailure(result)
                    }
                }
        )
    }

    private fun cancelFetchLastLawsTask() {
        if (fetchLastLawsTask?.isDisposed == false) {
            fetchLastLawsTask?.dispose()
            fetchLastLawsTask = null
        }
    }

    private fun handleGetLastLawsFailure(lawsOutput: FetchLawsUseCaseOutput) {
        if (lawsOutput.hasException() && lawsOutput.exception is VoxpException) {
            sendState(
                when ((lawsOutput.exception as VoxpException).exceptionType) {
                    CONNECTION -> LastLawsState.connectionError()
                    SERVER -> LastLawsState.serverError()
                    else -> LastLawsState.deviceError()
                }
            )
        } else {
            sendState(LastLawsState.deviceError())
        }
    }

    private fun pageListConfig(): PagedList.Config {
        return PagedList.Config.Builder()
            .setPageSize(20)
            .setPrefetchDistance(5)
            .setEnablePlaceholders(false)
            .build()
    }

    private fun mapLawsToPagedList(modelLaws: List<Law>?, total: Int): PagedList<LawCardState> {
        val result = mapLawsToState(modelLaws)

        return PagedList.Builder<Int, LawCardState>(LawsDataSource(result, total), pageListConfig())
            .setNotifyExecutor(ArchTaskExecutor.getMainThreadExecutor())
            .setFetchExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
            .build()
    }

    private fun mapLawsToState(modelLaws: List<Law>?): ArrayList<LawCardState> {
        val result = ArrayList<LawCardState>()
        if (modelLaws != null && modelLaws.isNotEmpty()) {
            for (law in modelLaws) {
                result.add(
                    LawCardState(
                        law.id ?: 0L,
                        law.name ?: "",
                        law.comments ?: "",
                        law.introductionDate ?: ""
                    )
                )
            }
        }
        return result
    }

    fun lastLawsRefreshSwiped() {
        requestLastLaws()
    }

    override fun errorPanelActionClicked() {
        requestLastLaws()
    }

    inner class LawsDataSource(
        private val initialLaws: List<LawCardState>,
        private val total: Int
    ) :
        PositionalDataSource<LawCardState>() {
        override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<LawCardState>) {
            collectDisposable(
                fetchLawsUseCase.execute(FetchLawsUseCaseInput(params.startPosition, params.loadSize))
                    .subscribe { result ->
                        when (result.status) {
                            SUCCESS -> callback.onResult(mapLawsToState(result.laws))
                            FAILURE -> handleGetLastLawsFailure(result)
                        }
                    }
            )
        }

        override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<LawCardState>) {
            callback.onResult(initialLaws, 0, total)
        }
    }
}