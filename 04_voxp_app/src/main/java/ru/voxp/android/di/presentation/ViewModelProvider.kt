package ru.voxp.android.di.presentation

import ru.jewelline.mvvm.base.presentation.ViewModelFactory

interface ViewModelProvider {
    fun getFactory(): ViewModelFactory
}
