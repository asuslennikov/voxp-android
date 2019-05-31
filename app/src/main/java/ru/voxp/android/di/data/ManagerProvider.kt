package ru.voxp.android.di.data

import ru.voxp.android.data.api.VoxpApi

interface ManagerProvider {

    fun getVoxpApi(): VoxpApi
}