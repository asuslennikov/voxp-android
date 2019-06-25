package ru.voxp.android.domain.di

import ru.voxp.android.domain.api.network.NetworkManager
import ru.voxp.android.domain.api.remote.RemoteRepository

interface UseCaseDependenciesProvider {
    fun getRemoteRepository(): RemoteRepository

    fun getNetworkManager(): NetworkManager
}