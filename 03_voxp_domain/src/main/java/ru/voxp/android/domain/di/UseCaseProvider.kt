package ru.voxp.android.domain.di

import ru.voxp.android.domain.usecase.FetchLastLawsUseCase
import ru.voxp.android.domain.usecase.NetworkGoesOnlineUseCase

interface UseCaseProvider {

    fun fetchLastLawsUseCase(): FetchLastLawsUseCase

    fun networkGoesOnlineUseCase(): NetworkGoesOnlineUseCase
}
