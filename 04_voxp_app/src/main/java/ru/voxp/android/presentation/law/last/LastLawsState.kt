package ru.voxp.android.presentation.law.last

import com.github.asuslennikov.mvvm.api.presentation.State
import ru.voxp.android.R
import ru.voxp.android.presentation.error.ErrorPanelState
import java.util.*

class LastLawsState : ErrorPanelState() {
    var loaderVisible: Boolean = false
    var lawsVisible: Boolean = false
    var searchExpanded: Boolean = false
    var laws: List<State> = Collections.emptyList()

    fun clone(): LastLawsState {
        val clone = LastLawsState()
        clone.loaderVisible = loaderVisible
        clone.lawsVisible = lawsVisible
        clone.searchExpanded = searchExpanded
        clone.laws = laws
        clone.errorPanelVisible = errorPanelVisible
        clone.errorPanelImage = errorPanelImage
        clone.errorPanelText = errorPanelText
        clone.errorPanelActionVisible = errorPanelActionVisible
        clone.errorPanelActionText = errorPanelActionText
        return clone
    }

    companion object {

        fun laws(laws: List<State>): LastLawsState {
            return LastLawsState().apply {
                lawsVisible = true
                this.laws = laws
            }
        }

        fun loading(): LastLawsState {
            return LastLawsState().apply {
                loaderVisible = true
            }
        }

        fun noInternet(): LastLawsState {
            return noInternet(LastLawsState())
        }

        fun connectionError(): LastLawsState {
            return connectionError(LastLawsState())
        }

        fun serverError(): LastLawsState {
            return serverError(LastLawsState())
        }

        fun deviceError(): LastLawsState {
            return deviceError(LastLawsState())
        }

        fun noResults(): LastLawsState {
            return LastLawsState().apply {
                errorPanelVisible = true;
                errorPanelImage = R.drawable.ic_no_result
                errorPanelText = R.string.error_panel_no_results_text
            }
        }
    }
}
