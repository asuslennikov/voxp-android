package ru.voxp.android.data.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import ru.voxp.android.di.data.ManagerScope
import ru.voxp.android.domain.api.network.NetworkManager
import javax.inject.Inject

@ManagerScope
class NetworkManagerImpl @Inject constructor(
    private val context: Context
) : NetworkManager {

    private val availabilityPublisher: BehaviorSubject<Boolean>

    init {
        availabilityPublisher = BehaviorSubject.createDefault(determineAvailabilityStatus())
        registerConnectionAvailabilityReceiver()
    }

    private fun determineAvailabilityStatus(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun registerConnectionAvailabilityReceiver() {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                availabilityPublisher.onNext(determineAvailabilityStatus())
            }
        }
        context.registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun isConnectionAvailable(): Boolean {
        return availabilityPublisher.value!!
    }

    override fun connectionAvailability(): Observable<Boolean> = availabilityPublisher
}