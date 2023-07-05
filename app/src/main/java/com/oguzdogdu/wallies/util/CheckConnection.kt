package com.oguzdogdu.wallies.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class CheckConnection @Inject constructor(private val context: Context) :
    MutableLiveData<Boolean?>() {

    private var broadcastReceiver: BroadcastReceiver? = null

    private fun prepareNetwork(context: Context) {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)

        broadcastReceiver = object : BroadcastReceiver() {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onReceive(p0: Context?, intent: Intent?) {
                val connectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

                val allNetworks =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

                value =
                    allNetworks != null && allNetworks.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_INTERNET
                )
            }
        }
        context.registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onActive() {
        prepareNetwork(context)
    }

    override fun onInactive() {
        context.unregisterReceiver(broadcastReceiver)
        broadcastReceiver = null
    }
}
