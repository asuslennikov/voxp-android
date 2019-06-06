package ru.voxp.android.presentation

import ru.jewelline.mvvm.base.domain.EmptyUseCaseInput
import ru.jewelline.mvvm.base.presentation.AbstractViewModel
import ru.jewelline.mvvm.interfaces.domain.UseCaseOutput.Status.IN_PROGRESS
import ru.jewelline.mvvm.interfaces.domain.UseCaseOutput.Status.SUCCESS
import ru.voxp.android.data.api.model.Law
import ru.voxp.android.domain.GetLastLawsUseCase
import javax.inject.Inject

class LastLawsViewModel @Inject constructor(private val lastLawsUseCase: GetLastLawsUseCase) :
    AbstractViewModel<LastLawsState>() {

    init {
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
                            loaderVisible = true
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

    private fun mapLawsToState(modelLaws: List<Law>?): List<LawListState> {
        val result = ArrayList<LawListState>()
        if (modelLaws != null && modelLaws.isNotEmpty()) {
            for (law in modelLaws) {
                result.add(
                    LawListState(
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