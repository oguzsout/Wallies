package com.oguzdogdu.wallies.util

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

@SuppressLint("StaticFieldLeak")
object CheckConnection : MutableLiveData<Boolean>() {

    private var broadcastReceiver: BroadcastReceiver? = null
    private var context: Context? = null

    fun init(context: Context) {
        this.context = context
    }

    private fun prepareNetwork(context: Context) {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)

        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, intent: Intent?) {
                val connectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

                val allNetworks = connectivityManager.allNetworks

                value = allNetworks.isNotEmpty()
            }
        }
        context.registerReceiver(broadcastReceiver, intentFilter)
    }


    override fun onActive() {
        prepareNetwork(context!!)
    }

    override fun onInactive() {
        context?.unregisterReceiver(broadcastReceiver)
        broadcastReceiver = null
    }
}