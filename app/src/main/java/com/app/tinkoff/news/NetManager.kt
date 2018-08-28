package com.app.tinkoff.news

import android.content.Context
import android.net.ConnectivityManager

class NetManager(private var applicationContext: Context) {

    val isConnected: Boolean?
        get() {
            val connectionManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectionManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
}