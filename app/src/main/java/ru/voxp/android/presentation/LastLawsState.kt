package ru.voxp.android.presentation

import ru.jewelline.mvvm.interfaces.presentation.State
import java.util.*

class LastLawsState : State {
    var loaderVisible: Boolean = false
    var lawsVisible: Boolean = false
    var laws: List<LawListState> = Collections.emptyList()
}
