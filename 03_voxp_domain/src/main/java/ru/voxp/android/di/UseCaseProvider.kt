package ru.voxp.android.di

import ru.voxp.android.domain.usecase.GetLastLawsUseCase

interface UseCaseProvider {

    fun getLastLawsUseCase(): GetLastLawsUseCase
}
