package ru.voxp.android

import android.app.Application
import ru.jewelline.mvvm.base.presentation.ViewModelFactory

class VoxpApplication : Application() {
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate() {
        super.onCreate()
        initializeComponents()
    }

    protected fun initializeComponents() {
        val registry = ComponentRegistry(this)
        viewModelFactory = registry.viewModelFactory
    }
}
