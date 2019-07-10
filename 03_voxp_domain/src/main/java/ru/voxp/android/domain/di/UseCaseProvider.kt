package ru.voxp.android.domain.di

import ru.voxp.android.domain.usecase.FetchLawsNetworkAwareUseCase
import ru.voxp.android.domain.usecase.FetchLawsUseCase
import ru.voxp.android.domain.usecase.NetworkGoesOnlineUseCase

interface UseCaseProvider {

    fun fetchLawsUseCase(): FetchLawsUseCase

    fun fetchLastLawsUseCase(): FetchLawsNetworkAwareUseCase

    fun networkGoesOnlineUseCase(): NetworkGoesOnlineUseCase
}
