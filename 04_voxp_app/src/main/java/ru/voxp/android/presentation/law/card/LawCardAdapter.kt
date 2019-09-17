package ru.voxp.android.presentation.law.card

import android.view.View
import android.view.ViewGroup
import io.supercharge.shimmerlayout.ShimmerLayout
import ru.jewelline.mvvm.base.presentation.ViewModelProvider
import ru.jewelline.mvvm.interfaces.presentation.State
import ru.voxp.android.R
import ru.voxp.android.presentation.core.recycler.AbstractRecyclerViewAdapter
import ru.voxp.android.presentation.core.recycler.BoundViewHolder

class LawCardViewHolder(itemView: View, viewModel: LawCardViewModel) :
    BoundViewHolder<LawCardState, LawCardViewModel>(itemView, viewModel)

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

class LawCardAdapter(viewModelProvider: ViewModelProvider.Linked) : AbstractRecyclerViewAdapter(viewModelProvider) {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoundViewHolder<out State, *> {
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