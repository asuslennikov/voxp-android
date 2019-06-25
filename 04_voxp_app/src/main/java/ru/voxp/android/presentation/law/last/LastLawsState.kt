package ru.voxp.android.presentation.law.last

import ru.jewelline.mvvm.interfaces.presentation.State
import ru.voxp.android.R
import ru.voxp.android.presentation.law.card.LawCardState
import java.util.*

class LastLawsState : State {
    var errorPanelVisible: Boolean = false
    var errorPanelImage: Int? = null
    var errorPanelText: Int? = null

    var loaderVisible: Boolean = false
    var lawsVisible: Boolean = false
    var laws: List<LawCardState> = Collections.emptyList()

    companion object {

        fun laws(laws: List<LawCardState>): LastLawsState {
            return LastLawsState().apply {
                lawsVisible = true
                this.laws = laws
            }
        }

        fun loading(connectionAvailable: Boolean): LastLawsState {
            if (connectionAvailable) {
                return loading()
            }
            return noInternet()
        }

        private fun loading(): LastLawsState {
            return LastLawsState().apply {
                loaderVisible = true
            }
        }

        private fun noInternet(): LastLawsState {
            return LastLawsState().apply {
                errorPanelVisible = true
                errorPanelImage = R.drawable.ic_no_internet
                errorPanelText = R.string.error_panel_no_internet_text
            }
        }

        fun connectionError(): LastLawsState {
            return LastLawsState().apply {
                errorPanelVisible = true
                errorPanelImage = R.drawable.ic_connection_error
                errorPanelText = R.string.error_panel_connection_error_text
            }
        }

        fun serverError(): LastLawsState {
            return LastLawsState().apply {
                errorPanelVisible = true
                errorPanelImage = R.drawable.ic_server_error
                errorPanelText = R.string.error_panel_server_error_text
            }
        }

        fun deviceError(): LastLawsState {
            return LastLawsState().apply {
                errorPanelVisible = true
                errorPanelImage = R.drawable.ic_device_error
                errorPanelText = R.string.error_panel_device_error_text
            }
        }
    }
}
