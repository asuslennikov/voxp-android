package ru.voxp.android.presentation.law.card

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.jewelline.mvvm.base.presentation.ViewModelProvider
import ru.voxp.android.R
import ru.voxp.android.presentation.core.recycler.AbstractRecyclerViewAdapter
import ru.voxp.android.presentation.core.recycler.BoundRecyclerViewHolder

class LawCardViewHolder(itemView: View) : BoundRecyclerViewHolder<LawCardState, LawCardViewModel>(itemView)

class LawCardAdapter(viewModelProvider: ViewModelProvider.Linked) :
    AbstractRecyclerViewAdapter<LawCardState, LawCardViewModel, LawCardViewHolder>(viewModelProvider) {

    companion object ViewType {
        private const val LAW = 0
        private const val LOADER = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) {
            LOADER
        } else {
            LAW
        }
    }

    override fun resolveViewModelClass(screen: LawCardViewHolder, position: Int): Class<LawCardViewModel> {
        return if (getItemViewType(position) == LAW) {
            LawCardViewModel::class.java
        } else {
            LawCardViewModel::class.java
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LawCardViewHolder {
        val layoutResourceId = if (viewType == LAW) {
            R.layout.law_card
        } else {
            R.layout.law_card_loader
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutResourceId, parent, false)
        return LawCardViewHolder(view)
    }
}