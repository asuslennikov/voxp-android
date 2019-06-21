package ru.voxp.android.presentation.law.last

import ru.jewelline.mvvm.base.domain.EmptyUseCaseInput
import ru.jewelline.mvvm.base.presentation.AbstractViewModel
import ru.jewelline.mvvm.interfaces.domain.UseCaseOutput.Status.IN_PROGRESS
import ru.jewelline.mvvm.interfaces.domain.UseCaseOutput.Status.SUCCESS
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

    fun requestLastLaws() {
        collectDisposable(
            lastLawsUseCase.execute(EmptyUseCaseInput.getInstance())
                .subscribe {
                    when (it.status) {
                        IN_PROGRESS -> sendState(LastLawsState().apply {
                            loaderVisible = it.connectionAvailable
                            noInternetVisible = !it.connectionAvailable
                            lawsVisible = false
                        })
                        SUCCESS -> sendState(LastLawsState().apply {
                            loaderVisible = false
                            lawsVisible = true
                            laws = mapLawsToState(it.laws)
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