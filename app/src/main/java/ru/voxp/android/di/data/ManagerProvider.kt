package ru.voxp.android.di.data

interface ManagerProvider {

    fun getRemoteRepository(): ru.voxp.android.domain.api.remote.RemoteRepository

    fun getNetworkManager(): ru.voxp.android.domain.api.network.NetworkManager
}