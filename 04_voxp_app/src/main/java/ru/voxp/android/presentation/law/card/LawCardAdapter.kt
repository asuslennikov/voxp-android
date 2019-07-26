package ru.voxp.android.presentation.law.card

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.voxp.android.R
import ru.voxp.android.presentation.core.recycler.AbstractRecyclerViewAdapter
import ru.voxp.android.presentation.core.recycler.BoundRecyclerViewHolder
import ru.voxp.android.presentation.core.recycler.ViewModelByStateProvider

class LawCardViewHolder(itemView: View) : BoundRecyclerViewHolder<LawCardState, LawCardViewModel>(itemView)

class LawCardAdapter(viewModelRegistry: ViewModelByStateProvider) :
    AbstractRecyclerViewAdapter<Long, LawCardState, LawCardViewModel, LawCardViewHolder>(viewModelRegistry) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LawCardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.law_card, parent, false)
        return LawCardViewHolder(view)
    }
}