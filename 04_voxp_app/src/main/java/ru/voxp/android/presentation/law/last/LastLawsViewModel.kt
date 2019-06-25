package ru.voxp.android.presentation.law.last

import ru.jewelline.mvvm.base.domain.EmptyUseCaseInput
import ru.jewelline.mvvm.base.presentation.AbstractViewModel
import ru.jewelline.mvvm.interfaces.domain.UseCaseOutput.Status.IN_PROGRESS
import ru.jewelline.mvvm.interfaces.domain.UseCaseOutput.Status.SUCCESS
import ru.voxp.android.R
import ru.voxp.android.domain.api.ExceptionType.*
import ru.voxp.android.domain.api.VoxpException
import ru.voxp.android.domain.api.model.Law
import ru.voxp.android.domain.usecase.GetLastLawsUseCase
import ru.voxp.android.presentation.core.recycler.ViewModelRegistry
import ru.voxp.android.presentation.law.card.LawCardState
import ru.voxp.android.presentation.law.card.LawCardViewModel
import javax.inject.Inject
import javax.inject.Provider

class LastLawsViewModel @Inject constructor(
    private val lastLawsUseCase: GetLastLawsUseCase,
    lawCardViewModelProvider: Provider<LawCardViewModel>
) : AbstractViewModel<LastLawsState>() {

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
                .subscribe {
                    when (it.status) {
                        IN_PROGRESS -> sendState(LastLawsState().apply {
                            if (it.connectionAvailable) {
                                loaderVisible = true
                                errorPanelVisible = false
                            } else {
                                loaderVisible = false
                                errorPanelVisible = true
                                errorPanelImage = R.drawable.ic_no_internet
                                errorPanelText = R.string.error_panel_no_internet_text
                            }
                            lawsVisible = false
                        })
                        SUCCESS -> sendState(LastLawsState().apply {
                            loaderVisible = false
                            errorPanelVisible = false
                            lawsVisible = true
                            laws = mapLawsToState(it.laws)
                        })
                        else -> sendState(LastLawsState().apply {
                            loaderVisible = false
                            errorPanelVisible = true
                            if (it.exception != null && it.exception is VoxpException) {
                                when ((it.exception as VoxpException).exceptionType){
                                    CONNECTION -> {
                                        errorPanelImage = R.drawable.ic_connection_error
                                        errorPanelText = R.string.error_panel_connection_error_text
                                    }
                                    SERVER -> {
                                        errorPanelImage = R.drawable.ic_server_error
                                        errorPanelText = R.string.error_panel_server_error_text
                                    }
                                    else -> {
                                        errorPanelImage = R.drawable.ic_device_error
                                        errorPanelText = R.string.error_panel_device_error_text
                                    }
                                }
                            } else {
                                errorPanelImage = R.drawable.ic_device_error
                                errorPanelText = R.string.error_panel_device_error_text
                            }
                            lawsVisible = false
                        })
                    }
                }
        )
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
}