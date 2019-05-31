package ru.voxp.android.di.data

import ru.voxp.android.data.api.VoxpManager

interface ManagerProvider {

    fun getVoxpManager(): VoxpManager
}