package ru.voxp.android.di.data

import ru.voxp.android.domain.api.network.NetworkManager
import ru.voxp.android.domain.api.remote.RemoteRepository

interface ManagerProvider {

    fun getRemoteRepository(): RemoteRepository

    fun getNetworkManager(): NetworkManager
}