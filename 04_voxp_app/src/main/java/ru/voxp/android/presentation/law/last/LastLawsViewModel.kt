package ru.voxp.android.presentation.law.last

import ru.jewelline.mvvm.base.domain.EmptyUseCaseInput
import ru.jewelline.mvvm.base.presentation.AbstractViewModel
import ru.jewelline.mvvm.interfaces.domain.UseCaseOutput.Status.*
import ru.voxp.android.domain.api.ExceptionType.CONNECTION
import ru.voxp.android.domain.api.ExceptionType.SERVER
import ru.voxp.android.domain.api.VoxpException
import ru.voxp.android.domain.api.model.Law
import ru.voxp.android.domain.usecase.GetLastLawsUseCase
import ru.voxp.android.domain.usecase.GetLastLawsUseCaseOutput
import ru.voxp.android.presentation.core.recycler.ViewModelRegistry
import ru.voxp.android.presentation.error.ErrorPanelViewModel
import ru.voxp.android.presentation.law.card.LawCardState
import ru.voxp.android.presentation.law.card.LawCardViewModel
import javax.inject.Inject
import javax.inject.Provider

class LastLawsViewModel @Inject constructor(
    private val lastLawsUseCase: GetLastLawsUseCase,
    lawCardViewModelProvider: Provider<LawCardViewModel>
) : AbstractViewModel<LastLawsState>(), ErrorPanelViewModel {

    val lawCardViewModelRegistry: ViewModelRegistry<Long, LawCardViewModel>

    init {
        lawCardViewModelRegistry = ViewModelRegistry(lawCardViewModelProvider)
        requestLastLaws()
    }

    override fun buildInitialState(): LastLawsState {
        return LastLawsState()
    }

    private fun requestLastLaws() {
        collectDisposable(
            lastLawsUseCase.execute(EmptyUseCaseInput.getInstance())
                .subscribe { lastLawsOutput ->
                    when (lastLawsOutput.status) {
                        IN_PROGRESS -> sendState(LastLawsState.loading(lastLawsOutput.connectionAvailable))
                        SUCCESS -> sendState(LastLawsState.laws(mapLawsToState(lastLawsOutput.laws)))
                        FAILURE -> handleGetLastLawsFailure(lastLawsOutput)
                    }
                }
        )
    }

    private fun handleGetLastLawsFailure(lastLawsOutput: GetLastLawsUseCaseOutput) {
        if (lastLawsOutput.hasException() && lastLawsOutput.exception is VoxpException) {
            sendState(
                when ((lastLawsOutput.exception as VoxpException).exceptionType) {
                    CONNECTION -> LastLawsState.connectionError()
                    SERVER -> LastLawsState.serverError()
                    else -> LastLawsState.deviceError()
                }
            )
        } else {
            sendState(LastLawsState.deviceError())
        }
    }

    private fun mapLawsToState(modelLaws: List<Law>?): List<LawCardState> {
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

    override fun errorPanelActionClicked() {
        requestLastLaws()
    }
}