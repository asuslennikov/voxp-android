package ru.voxp.android.presentation.law.card

import ru.jewelline.mvvm.interfaces.presentation.Screen
import ru.voxp.android.R
import ru.voxp.android.presentation.core.recycler.RecyclerViewModel
import javax.inject.Inject

class LawCardViewModel @Inject constructor() : RecyclerViewModel<LawCardState>() {

    fun onCardClick(screen: Screen<LawCardState>) {
        val law = getCurrentState(screen)
        if (law.url != null) {
            sendEffect(screen, OpenUrlInBrowser(law.url))
        } else {
            sendEffect(screen, ShowSnackbarMessageEffect(R.string.law_card_no_law_url))
        }
    }
}