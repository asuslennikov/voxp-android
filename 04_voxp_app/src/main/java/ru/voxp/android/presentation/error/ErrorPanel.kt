package ru.voxp.android.presentation.error

import ru.jewelline.mvvm.interfaces.presentation.State

open class ErrorPanelState : State {
    var errorPanelVisible: Boolean = false
    var errorPanelImage: Int? = null
    var errorPanelText: Int? = null
    var errorPanelActionVisible: Boolean = false
    var errorPanelActionText: Int? = null
}