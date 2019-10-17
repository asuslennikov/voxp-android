package ru.voxp.android.presentation.law.card

import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import io.supercharge.shimmerlayout.ShimmerLayout
import ru.jewelline.mvvm.base.presentation.ViewModelProvider
import ru.jewelline.mvvm.interfaces.presentation.Effect
import ru.jewelline.mvvm.interfaces.presentation.State
import ru.voxp.android.BR
import ru.voxp.android.R
import ru.voxp.android.presentation.core.recycler.AbstractRecyclerViewAdapter
import ru.voxp.android.presentation.core.recycler.BoundViewHolder

class LawCardViewHolder(itemView: View, viewModel: LawCardViewModel) :
    BoundViewHolder<LawCardState, LawCardViewModel>(itemView, viewModel) {

    override fun getBindingScreenVariableId(): Int = BR.screen

    override fun holderSupportsEffects(): Boolean = true

    override fun applyEffect(screenEffect: Effect) {
        when (screenEffect) {
            is ShowSnackbarMessageEffect -> showSnackbarMessage(screenEffect.messageId)
            is OpenUrlInBrowser -> openUrl(screenEffect.url)
        }
    }

    private fun showSnackbarMessage(messageId: Int) =
        Snackbar.make(itemView, messageId, Snackbar.LENGTH_LONG).show()

    private fun openUrl(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)

        if (itemView.context != null && intent.resolveActivity(itemView.context.packageManager) != null) {
            itemView.context.startActivity(intent)
        } else {
            showSnackbarMessage(R.string.law_card_can_not_open_url)
        }
    }
}

class LawLoaderViewHolder(itemView: View, viewModel: LawLoaderViewModel) :
    BoundViewHolder<LawLoaderState, LawLoaderViewModel>(itemView, viewModel) {

    private val shimmerLayout: ShimmerLayout = itemView.findViewById(R.id.law_card_loader_container)

    override fun render(screenState: LawLoaderState) {
        super.render(screenState)
        if (screenState.showAnimation) {
            shimmerLayout.startShimmerAnimation()
        } else {
            shimmerLayout.stopShimmerAnimation()
        }
    }
}

class LawCardAdapter(viewModelProvider: ViewModelProvider.Linked) :
    AbstractRecyclerViewAdapter(viewModelProvider) {

    private companion object ViewType {
        const val LAW = 0
        const val LOADER = 1
    }

    override fun getItemViewType(position: Int): Int {
        if (getItem(position) is LawLoaderState) {
            return LOADER
        }
        return LAW
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BoundViewHolder<out State, *> {
        return if (viewType == LAW) {
            LawCardViewHolder(
                inflate(parent, R.layout.law_card),
                createViewModel(LawCardViewModel::class.java)
            )
        } else {
            LawLoaderViewHolder(
                inflate(parent, R.layout.law_card_loader),
                createViewModel(LawLoaderViewModel::class.java)
            )
        }
    }
}