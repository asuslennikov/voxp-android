package ru.voxp.android.presentation.core.recycler

import ru.jewelline.mvvm.base.presentation.ViewModelFactory
import ru.jewelline.mvvm.interfaces.HasKey
import ru.jewelline.mvvm.interfaces.presentation.State
import ru.jewelline.mvvm.interfaces.presentation.ViewModel
import ru.voxp.android.presentation.di.PresentationScope
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

@PresentationScope
class ViewModelByStateProvider @Inject constructor(private val viewModelFactory: ViewModelFactory) {
    private val viewModelMapping: MutableMap<KClass<out State>, Provider<out ViewModel<out State>?>> = HashMap()
    private val viewModelCache: MutableMap<KClass<out State>, MutableMap<Any, ViewModel<out State>>> = HashMap()

    fun <STATE : State> registerMapping(
        stateClass: KClass<STATE>,
        viewModelClass: KClass<out ViewModel<STATE>>
    ): ViewModelByStateProvider {
        viewModelMapping[stateClass] = Provider { viewModelFactory.create(viewModelClass.java) }
        return this
    }

    fun <STATE : State> registerMapping(
        stateClass: KClass<STATE>,
        viewModelFactory: Provider<out ViewModel<STATE>>
    ): ViewModelByStateProvider {
        viewModelMapping[stateClass] = viewModelFactory
        return this
    }

    fun <STATE : State> getViewModel(state: STATE): ViewModel<STATE> {
        var viewModelsPerState = viewModelCache[state::class]
        if (viewModelsPerState == null) {
            viewModelsPerState = HashMap()
            viewModelCache[state::class] = viewModelsPerState
        }
        val key = if (state is HasKey<*>) state.key else state::class
        var viewModel = viewModelsPerState[key]
        if (viewModel == null){
            val provider = viewModelMapping[state::class]
            viewModel = provider?.get()
            if (viewModel == null){
                throw IllegalArgumentException("Can't handle " + state::class.java.simpleName + " state class")
            }
            viewModelsPerState[key] = viewModel
        }
        return viewModel as ViewModel<STATE>
    }
}