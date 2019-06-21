package ru.voxp.android.presentation.law.last

import ru.jewelline.mvvm.interfaces.presentation.State
import ru.voxp.android.presentation.law.card.LawCardState
import java.util.*

class LastLawsState : State {
    var loaderVisible: Boolean = false
    var noInternetVisible: Boolean = false
    var lawsVisible: Boolean = false
    var laws: List<LawCardState> = Collections.emptyList()
}
