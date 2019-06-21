package ru.voxp.android.di.domain

import ru.voxp.android.domain.usecase.GetLastLawsUseCase

interface UseCaseProvider {

    fun getLastLawsUseCase(): GetLastLawsUseCase
}
