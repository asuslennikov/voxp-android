package ru.voxp.android.presentation.core.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.jewelline.mvvm.base.presentation.ViewModelProvider
import ru.jewelline.mvvm.interfaces.presentation.State
import ru.jewelline.mvvm.interfaces.presentation.ViewModel

abstract class AbstractRecyclerViewAdapter(private val viewModelProvider: ViewModelProvider.Linked) :
    ListAdapter<State, BoundViewHolder<out State, *>>(StateListItemCallback()) {

    protected fun inflate(parent: ViewGroup, layoutResourceId: Int): View {
        return LayoutInflater.from(parent.context).inflate(layoutResourceId, parent, false)
    }

    protected fun <STATE : State, VM : ViewModel<STATE>> createViewModel(viewModelClass: Class<VM>): VM {
        return viewModelProvider.getViewModel(viewModelClass)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: BoundViewHolder<out State, *>, position: Int) {
        (holder as BoundViewHolder<State, *>).bind(getItem(position))
    }
}