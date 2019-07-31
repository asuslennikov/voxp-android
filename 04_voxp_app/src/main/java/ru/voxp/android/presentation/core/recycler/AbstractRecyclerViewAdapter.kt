package ru.voxp.android.presentation.core.recycler

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.jewelline.mvvm.base.presentation.ViewModelProvider
import ru.jewelline.mvvm.interfaces.presentation.State
import ru.jewelline.mvvm.interfaces.presentation.ViewModel

abstract class AbstractRecyclerViewAdapter<STATE : State, VM : ViewModel<STATE>, SCREEN : BoundRecyclerViewHolder<STATE, VM>>
constructor(private val viewModelProvider: ViewModelProvider.Linked) :
    ListAdapter<STATE, SCREEN>(object : DiffUtil.ItemCallback<STATE>() {
        override fun areItemsTheSame(oldItem: STATE, newItem: STATE): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: STATE, newItem: STATE): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }
    }) {

    override fun onBindViewHolder(screen: SCREEN, position: Int) {
        if (position == itemCount - 1) {
            return
        }
        val state = getItem(position)
        if (state != null) {
            val viewModel = viewModelProvider.getViewModel(resolveViewModelClass(screen, position))
            screen.bind(state, viewModel)
        }
    }

    protected abstract fun resolveViewModelClass(screen: SCREEN, position: Int): Class<out VM>
}