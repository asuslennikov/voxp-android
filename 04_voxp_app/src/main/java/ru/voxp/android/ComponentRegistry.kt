package ru.voxp.android

import android.content.Context
import com.github.asuslennikov.mvvm.presentation.ViewModelProvider
import ru.voxp.android.data.di.DaggerManagerComponent
import ru.voxp.android.di.ApplicationModule
import ru.voxp.android.di.DaggerApplicationComponent
import ru.voxp.android.domain.di.DaggerUseCaseComponent
import ru.voxp.android.presentation.di.DaggerPresentationComponent
import ru.voxp.android.presentation.di.PresentationProvider

internal class ComponentRegistry(context: Context) {
    private var presentationProvider: PresentationProvider? = null

    val viewModelProvider: ViewModelProvider
        get() = presentationProvider!!.getViewModelProvider()

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
        presentationProvider = DaggerPresentationComponent.builder()
            .applicationProvider(applicationProvider)
            .managerProvider(managerProvider)
            .useCaseProvider(useCaseProvider)
            .build()
    }
}
