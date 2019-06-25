package ru.voxp.android

import android.content.Context
import ru.jewelline.mvvm.base.presentation.ViewModelFactory
import ru.voxp.android.data.di.DaggerManagerComponent
import ru.voxp.android.domain.di.DaggerUseCaseComponent
import ru.voxp.android.di.ApplicationModule
import ru.voxp.android.di.DaggerApplicationComponent
import ru.voxp.android.presentation.di.DaggerViewModelComponent
import ru.voxp.android.presentation.di.ViewModelProvider

internal class ComponentRegistry(context: Context) {
    private var viewModelProvider: ViewModelProvider? = null

    val viewModelFactory: ViewModelFactory
        get() = viewModelProvider!!.getFactory()

    init {
        initializeRegistry(context)
    }

    private fun initializeRegistry(context: Context) {
        val applicationProvider = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(context))
            .build()
        val managerProvider = DaggerManagerComponent.builder()
            .applicationProvider(applicationProvider)
            .build()
        val useCaseProvider = DaggerUseCaseComponent.builder()
            .useCaseDependenciesProvider(managerProvider)
            .build()
        viewModelProvider = DaggerViewModelComponent.builder()
            .applicationProvider(applicationProvider)
            .managerProvider(managerProvider)
            .useCaseProvider(useCaseProvider)
            .build()
    }
}
