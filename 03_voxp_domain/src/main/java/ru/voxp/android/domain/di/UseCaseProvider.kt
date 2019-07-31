package ru.voxp.android.domain.di

import ru.voxp.android.domain.usecase.NetworkGoesOnlineUseCase
import ru.voxp.android.domain.usecase.SearchLawsUseCase

interface UseCaseProvider {

    fun networkGoesOnlineUseCase(): NetworkGoesOnlineUseCase

    fun searchLawsUseCase(): SearchLawsUseCase
}
