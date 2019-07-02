package ru.voxp.android.data.network

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import android.os.Build
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import ru.voxp.android.data.di.ManagerScope
import ru.voxp.android.domain.api.network.NetworkManager
import javax.inject.Inject

@ManagerScope
class NetworkManagerImpl @Inject constructor(
    private val context: Context
) : NetworkManager {

    private val availabilityPublisher: BehaviorSubject<Boolean>
    private val availabilityObservable: Observable<Boolean>

    init {
        availabilityPublisher = BehaviorSubject.createDefault(determineAvailabilityStatus())
        availabilityObservable = availabilityPublisher.distinctUntilChanged()
        registerConnectionAvailabilityWatcher()
    }

    private fun determineAvailabilityStatus(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun registerConnectionAvailabilityWatcher() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            newConnectionAvailabilityWatcher()
        } else {
            oldConnectionAvailabilityWatcher()
        }
    }

    private fun oldConnectionAvailabilityWatcher() {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                availabilityPublisher.onNext(determineAvailabilityStatus())
            }
        }
        context.registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun newConnectionAvailabilityWatcher() {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkRequestBuilder = NetworkRequest.Builder()
            .addCapability(NET_CAPABILITY_INTERNET)
        val networkRequest = networkRequestBuilder.build()
        connectivityManager.registerNetworkCallback(networkRequest, object : NetworkCallback() {
            override fun onAvailable(network: Network?) {
                availabilityPublisher.onNext(determineAvailabilityStatus())
            }

            override fun onLost(network: Network?) {
                availabilityPublisher.onNext(determineAvailabilityStatus())
            }
        })
    }

    override fun isConnectionAvailable(): Boolean {
        return availabilityPublisher.value!!
    }

    override fun connectionAvailability(): Observable<Boolean> = availabilityObservable
}