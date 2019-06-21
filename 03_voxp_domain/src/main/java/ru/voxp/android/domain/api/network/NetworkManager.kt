package ru.voxp.android.domain.api.network

import io.reactivex.Observable

interface NetworkManager {
    fun isConnectionAvailable(): Boolean

    fun connectionAvailability(): Observable<Boolean>
}