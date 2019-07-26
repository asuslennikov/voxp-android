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

    override fun resolveViewModelClass(screen: LawCardViewHolder): Class<LawCardViewModel> {
        return LawCardViewModel::class.java
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LawCardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.law_card, parent, false)
        return LawCardViewHolder(view)
    }
}