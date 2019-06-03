package ru.voxp.android.presentation

import ru.jewelline.mvvm.interfaces.presentation.State
import ru.voxp.android.data.api.model.Law
import java.util.*

class LastLawsState : State {
    var loaderVisible: Boolean = false
    var lawsVisible: Boolean = false
    var laws: List<Law> = Collections.emptyList()
}
