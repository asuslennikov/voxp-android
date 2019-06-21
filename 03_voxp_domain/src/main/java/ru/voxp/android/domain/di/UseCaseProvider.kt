package ru.voxp.android.domain.di

import ru.voxp.android.domain.usecase.GetLastLawsUseCase

interface UseCaseProvider {

    fun getLastLawsUseCase(): GetLastLawsUseCase
}
