package ru.voxp.android.presentation

import android.util.Log
import ru.jewelline.mvvm.base.domain.EmptyUseCaseInput
import ru.jewelline.mvvm.base.presentation.AbstractViewModel
import ru.voxp.android.data.GetLastLawsUseCase
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
                    Log.d("LastLawsViewModel", "Status = " + it.status)
                }
        );
    }
}