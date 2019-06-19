package ru.voxp.android.presentation.core.recycler

import androidx.recyclerview.widget.RecyclerView.Adapter
import ru.jewelline.mvvm.interfaces.HasKey
import ru.jewelline.mvvm.interfaces.presentation.State
import ru.jewelline.mvvm.interfaces.presentation.ViewModel
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Provider

abstract class AbstractRecyclerViewAdapter<KEY, STATE, VM : ViewModel<STATE>, SCREEN : BoundRecyclerViewHolder<STATE, VM>>
constructor(viewModelProvider: Provider<VM>) :
    Adapter<SCREEN>() where STATE : State, STATE : HasKey<KEY> {

    private lateinit var content: List<STATE>
    private val viewModelRegistry: ModelRegistry<KEY, VM> = ModelRegistry(viewModelProvider)

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

private class ModelRegistry<KEY, VM : ViewModel<out State>>(private val viewModelProvider: Provider<VM>) {
    private val modelMap: MutableMap<KEY, VM> = ConcurrentHashMap()

    fun getModel(key: KEY): VM {
        var viewModel = modelMap[key]
        if (viewModel == null) {
            viewModel = viewModelProvider.get();
            modelMap[key] = viewModel
        }
        return viewModel!!
    }
}