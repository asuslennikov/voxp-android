package ru.voxp.android.presentation.core.recycler

import ru.jewelline.mvvm.interfaces.presentation.State
import ru.jewelline.mvvm.interfaces.presentation.ViewModel
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Provider

class ViewModelRegistry<KEY, VM : ViewModel<out State>>(private val viewModelProvider: Provider<VM>) {
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