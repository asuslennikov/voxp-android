package ru.voxp.android.presentation.di

import ru.jewelline.mvvm.base.presentation.ViewModelFactory

interface ViewModelProvider {
    fun getFactory(): ViewModelFactory
}
