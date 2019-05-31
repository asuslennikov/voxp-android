package ru.voxp.android.di.domain

import ru.voxp.android.data.GetLastLawsUseCase

interface UseCaseProvider {

    fun getLastLawsUseCase(): GetLastLawsUseCase
}
