package ru.voxp.android.presentation.law.last

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
                                LastLawsState.laws(mapLawsToState(result.getData()))
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
}