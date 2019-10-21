package ru.voxp.android.presentation.law.last

import android.view.inputmethod.EditorInfo
import com.github.asuslennikov.mvvm.api.domain.UseCaseOutput.Status.*
import com.github.asuslennikov.mvvm.api.presentation.State
import com.github.asuslennikov.mvvm.presentation.AbstractViewModel
import io.reactivex.disposables.Disposable
import ru.voxp.android.domain.api.ExceptionType.*
import ru.voxp.android.domain.api.VoxpException
import ru.voxp.android.domain.usecase.SearchLawsInput
import ru.voxp.android.domain.usecase.SearchLawsOutput
import ru.voxp.android.domain.usecase.SearchLawsUseCase
import ru.voxp.android.presentation.error.ErrorPanelViewModel
import ru.voxp.android.presentation.law.card.LawCardState
import ru.voxp.android.presentation.law.card.LawLoaderState
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
        val searchLawsInput = SearchLawsInput(name = currentState.searchText)
        searchLawsTaskDisposable = collectDisposable(
            searchLawsUseCase.execute(searchLawsInput)
                .subscribe { result ->
                    searchLawsTaskKey = result.getKey()
                    when (result.getStatus()) {
                        IN_PROGRESS -> {
                            if (result.getData().isEmpty()) {
                                sendState(LastLawsState.loading())
                            }
                        }
                        SUCCESS -> sendState(
                            if (result.getTotal() == 0) {
                                LastLawsState.noResults()
                            } else {
                                LastLawsState.laws(mapLawsToState(result))
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
        if (searchLawsOutput.getData().isNotEmpty()) {
            return
        }
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

    private fun mapLawsToState(searchLawsResult: SearchLawsOutput): List<State> {
        val result = ArrayList<State>()
        val modelLaws = searchLawsResult.getData()
        for (law in modelLaws) {
            result.add(
                LawCardState(
                    law.id?.toString() ?: "none",
                    law.name ?: "",
                    law.comments ?: "",
                    law.introductionDate ?: "",
                    law.url
                )
            )
        }
        if (searchLawsResult.getTotal() > searchLawsResult.getData().size) {
            result.add(LawLoaderState(searchLawsTaskKey!!))
        }
        return result
    }

    fun lastLawsRefreshSwiped() {
        requestLastLaws()
    }

    override fun errorPanelActionClicked() {
        requestLastLaws()
    }

    fun triggerNextPageLoading() {
        searchLawsUseCase.triggerNextPageLoading(SearchLawsInput(searchLawsTaskKey))
    }

    fun onSearchClick() {
        sendState(currentState.clone().apply {
            searchExpanded = !currentState.searchExpanded
        })
    }

    fun onSearchTriggered(actionId: Int) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            requestLastLaws()
        }
    }
}