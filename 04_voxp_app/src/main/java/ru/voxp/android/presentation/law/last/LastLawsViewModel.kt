package ru.voxp.android.presentation.law.last

import android.os.AsyncTask
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.paging.PagedList
import androidx.paging.PositionalDataSource
import io.reactivex.disposables.Disposable
import ru.jewelline.mvvm.base.presentation.AbstractViewModel
import ru.jewelline.mvvm.interfaces.domain.UseCaseOutput.Status.*
import ru.voxp.android.domain.api.ExceptionType.*
import ru.voxp.android.domain.api.VoxpException
import ru.voxp.android.domain.api.model.Law
import ru.voxp.android.domain.usecase.SearchLawsInput
import ru.voxp.android.domain.usecase.SearchLawsOutput
import ru.voxp.android.domain.usecase.SearchLawsUseCase
import ru.voxp.android.presentation.error.ErrorPanelViewModel
import ru.voxp.android.presentation.law.card.LawCardState
import javax.inject.Inject

class LastLawsViewModel @Inject constructor(
    private val searchLawsUseCase: SearchLawsUseCase
) : AbstractViewModel<LastLawsState>(), ErrorPanelViewModel {

    private var searchLawsTaskDisposable: Disposable? = null
    private var searchLawsTaskKey: String? = null

    init {
        requestLastLaws()
    }

    override fun buildInitialState(): LastLawsState {
        return LastLawsState()
    }

    private fun requestLastLaws() {
        cancelFetchLastLawsTask()
        searchLawsTaskDisposable = collectDisposable(
            searchLawsUseCase.execute(SearchLawsInput())
                .subscribe { result ->
                    searchLawsTaskKey = result.getKey()
                    when (result.getStatus()) {
                        IN_PROGRESS -> sendState(LastLawsState.loading())
                        SUCCESS -> sendState(
                            if (result.getTotal() == 0) {
                                LastLawsState.noResults()
                            } else {
                                LastLawsState.laws(
                                    mapLawsToPagedList(result.getData(), result.getTotal())
                                )
                            }
                        )
                        FAILURE -> handleGetLastLawsFailure(result)
                    }
                }
        )
    }

    private fun cancelFetchLastLawsTask() {
        if (searchLawsTaskDisposable?.isDisposed == false) {
            searchLawsTaskDisposable?.dispose()
            searchLawsTaskDisposable = null
        }
    }

    private fun handleGetLastLawsFailure(searchLawsOutput: SearchLawsOutput) {
        if (searchLawsOutput.getException() is VoxpException) {
            sendState(
                when ((searchLawsOutput.getException() as VoxpException).exceptionType) {
                    NO_CONNECTION_AVAILABLE -> LastLawsState.noInternet()
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
            searchLawsUseCase.triggerNextPageLoading(SearchLawsInput(searchLawsTaskKey))
        }

        override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<LawCardState>) {
            callback.onResult(initialLaws, 0, total)
        }
    }
}