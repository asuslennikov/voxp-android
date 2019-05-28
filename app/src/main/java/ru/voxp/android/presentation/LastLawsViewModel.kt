package ru.voxp.android.presentation

import ru.jewelline.mvvm.base.presentation.AbstractViewModel
import javax.inject.Inject

class LastLawsViewModel @Inject constructor() : AbstractViewModel<LastLawsState>() {
    override fun buildInitialState(): LastLawsState {
        return LastLawsState()
    }
}