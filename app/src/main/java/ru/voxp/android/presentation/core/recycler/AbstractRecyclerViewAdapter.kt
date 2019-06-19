package ru.voxp.android.presentation.core.recycler

import androidx.recyclerview.widget.RecyclerView.Adapter
import ru.jewelline.mvvm.interfaces.HasKey
import ru.jewelline.mvvm.interfaces.presentation.State
import ru.jewelline.mvvm.interfaces.presentation.ViewModel

abstract class AbstractRecyclerViewAdapter<KEY, STATE, VM : ViewModel<STATE>, SCREEN : BoundRecyclerViewHolder<STATE, VM>>
constructor(private val viewModelRegistry: ViewModelRegistry<KEY, VM>) :
    Adapter<SCREEN>() where STATE : State, STATE : HasKey<KEY> {

    private lateinit var content: List<STATE>

    fun setData(items: List<STATE>) {
        content = items
    }

    override fun getItemCount(): Int = content.size

    override fun onBindViewHolder(holder: SCREEN, position: Int) {
        val state = content[position]
        val viewModel = viewModelRegistry.getModel(state.key)
        holder.bindHolder(Pair(state, viewModel))
    }
}