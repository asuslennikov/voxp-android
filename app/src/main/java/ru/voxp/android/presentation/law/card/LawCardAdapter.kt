package ru.voxp.android.presentation.law.card

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.voxp.android.R
import ru.voxp.android.presentation.core.recycler.AbstractRecyclerViewAdapter
import ru.voxp.android.presentation.core.recycler.BoundRecyclerViewHolder
import javax.inject.Provider

class LawCardViewHolder(itemView: View) : BoundRecyclerViewHolder<LawCardState, LawCardViewModel>(itemView)

class LawCardAdapter(viewModelProvider: Provider<LawCardViewModel>) :
    AbstractRecyclerViewAdapter<Long, LawCardState, LawCardViewModel, LawCardViewHolder>(viewModelProvider) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LawCardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.law_list_view, parent, false)
        return LawCardViewHolder(view)
    }
}