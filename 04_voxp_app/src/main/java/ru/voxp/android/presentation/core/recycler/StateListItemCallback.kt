package ru.voxp.android.presentation.core.recycler

import androidx.recyclerview.widget.DiffUtil
import ru.jewelline.mvvm.interfaces.presentation.State

internal class StateListItemCallback<T : State> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}