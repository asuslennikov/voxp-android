package ru.voxp.android.presentation.law.card

import ru.jewelline.mvvm.base.presentation.AbstractViewModel
import javax.inject.Inject

class LawCardViewModel @Inject constructor() : AbstractViewModel<LawCardState>() {
    override fun buildInitialState(): LawCardState =
        throw IllegalStateException("This view model usually should newer create new state by itself")


}