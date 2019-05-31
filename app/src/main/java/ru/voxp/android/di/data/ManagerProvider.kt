package ru.voxp.android.di.data

import ru.voxp.android.data.VoxpApi

interface ManagerProvider {

    fun getVoxpApi(): VoxpApi
}