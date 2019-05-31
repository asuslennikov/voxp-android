package ru.voxp.android

import android.content.Context
import ru.jewelline.mvvm.base.presentation.ViewModelFactory
import ru.voxp.android.di.ApplicationModule
import ru.voxp.android.di.DaggerApplicationComponent
import ru.voxp.android.di.data.DaggerManagerComponent
import ru.voxp.android.di.data.ManagerModule
import ru.voxp.android.di.domain.DaggerUseCaseComponent
import ru.voxp.android.di.presentation.DaggerViewModelComponent
import ru.voxp.android.di.presentation.ViewModelProvider

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
            .managerModule(ManagerModule)
            .build()
        val useCaseProvider = DaggerUseCaseComponent.builder()
            .managerProvider(managerProvider)
            .build()
        viewModelProvider = DaggerViewModelComponent.builder()
            .applicationProvider(applicationProvider)
            .managerProvider(managerProvider)
            .useCaseProvider(useCaseProvider)
            .build()
    }
}
