package ru.voxp.android.di.domain

import ru.voxp.android.domain.GetLastLawsUseCase

interface UseCaseProvider {

    fun getLastLawsUseCase(): GetLastLawsUseCase
}
