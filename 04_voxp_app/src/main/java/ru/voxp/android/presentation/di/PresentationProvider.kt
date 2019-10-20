package ru.voxp.android.presentation.di

import com.github.asuslennikov.mvvm.presentation.ViewModelProvider

interface PresentationProvider {
    fun getViewModelProvider(): ViewModelProvider
}
