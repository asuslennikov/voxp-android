package ru.voxp.android.presentation.error

import ru.jewelline.mvvm.interfaces.presentation.State
import ru.voxp.android.R

open class ErrorPanelState : State {
    var errorPanelVisible: Boolean = false
    var errorPanelImage: Int? = null
    var errorPanelText: Int? = null
    var errorPanelActionVisible: Boolean = false
    var errorPanelActionText: Int? = null

    protected companion object {
        fun <T : ErrorPanelState> noInternet(state: T): T {
            return state.apply {
                errorPanelVisible = true
                errorPanelImage = R.drawable.ic_no_internet
                errorPanelText = R.string.error_panel_no_internet_text
            }
        }

        fun <T : ErrorPanelState> connectionError(state: T): T {
            return state.apply {
                errorPanelVisible = true
                errorPanelImage = R.drawable.ic_connection_error
                errorPanelText = R.string.error_panel_connection_error_text
                errorPanelActionVisible = true
                errorPanelActionText = R.string.error_panel_retry_action_text
            }
        }

        fun <T : ErrorPanelState> serverError(state: T): T {
            return state.apply {
                errorPanelVisible = true
                errorPanelImage = R.drawable.ic_server_error
                errorPanelText = R.string.error_panel_server_error_text
                errorPanelActionVisible = true
                errorPanelActionText = R.string.error_panel_retry_action_text
            }
        }

        fun <T : ErrorPanelState> deviceError(state: T): T {
            return state.apply {
                errorPanelVisible = true
                errorPanelImage = R.drawable.ic_device_error
                errorPanelText = R.string.error_panel_device_error_text
                errorPanelActionVisible = true
                errorPanelActionText = R.string.error_panel_retry_action_text
            }
        }
    }
}

interface ErrorPanelViewModel {
    fun errorPanelActionClicked()
}