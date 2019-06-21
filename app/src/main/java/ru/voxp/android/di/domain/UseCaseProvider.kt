package ru.voxp.android.di.domain

interface UseCaseProvider {

    fun getLastLawsUseCase(): ru.voxp.android.domain.usecase.GetLastLawsUseCase
}
