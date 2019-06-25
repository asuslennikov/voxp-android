package ru.voxp.android.domain.di

import ru.voxp.android.domain.usecase.GetLastLawsUseCase
import ru.voxp.android.domain.usecase.NetworkSwitchUseCase

interface UseCaseProvider {

    fun getLastLawsUseCase(): GetLastLawsUseCase

    fun networkSwitchUseCase(): NetworkSwitchUseCase
}
