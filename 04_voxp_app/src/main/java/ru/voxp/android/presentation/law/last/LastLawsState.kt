package ru.voxp.android.presentation.law.last

import ru.jewelline.mvvm.interfaces.presentation.State
import ru.voxp.android.presentation.law.card.LawCardState
import java.util.*

class LastLawsState : State {
    var errorPanelVisible: Boolean = false
    var errorPanelImage: Int? = null
    var errorPanelText: Int? = null

    var loaderVisible: Boolean = false
    var lawsVisible: Boolean = false
    var laws: List<LawCardState> = Collections.emptyList()
}
