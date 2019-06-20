package ru.voxp.android.di.data

import ru.voxp.android.data.api.VoxpManager
import ru.voxp.android.domain.api.network.NetworkManager

interface ManagerProvider {

    fun getVoxpManager(): VoxpManager

    fun getNetworkManager(): NetworkManager
}