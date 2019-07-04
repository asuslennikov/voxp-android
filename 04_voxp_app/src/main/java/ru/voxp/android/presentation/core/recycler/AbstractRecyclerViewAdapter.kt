package ru.voxp.android.presentation.core.recycler

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.jewelline.mvvm.interfaces.HasKey
import ru.jewelline.mvvm.interfaces.presentation.State
import ru.jewelline.mvvm.interfaces.presentation.ViewModel

abstract class AbstractRecyclerViewAdapter<KEY, STATE, VM : ViewModel<STATE>, SCREEN : BoundRecyclerViewHolder<STATE, VM>>
constructor(private val viewModelRegistry: ViewModelRegistry<KEY, VM>) :
    ListAdapter<STATE, SCREEN>(object : DiffUtil.ItemCallback<STATE>() {
        override fun areItemsTheSame(oldItem: STATE, newItem: STATE): Boolean {
            return oldItem.key == newItem.key
        }

        override fun areContentsTheSame(oldItem: STATE, newItem: STATE): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }
    }) where STATE : State, STATE : HasKey<KEY> {

    override fun onBindViewHolder(holder: SCREEN, position: Int) {
        val state = getItem(position)
        val viewModel = viewModelRegistry.getModel(state.key)
        holder.bindHolder(Pair(state, viewModel))
    }
}