package com.example.gifapp.utils.network

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build

class NetworkUtil(private var context: Context) {

    fun isNetworkAvailable(): Boolean {
        val cm: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm.activeNetwork
        } else {
            cm.activeNetworkInfo
        }
        return activeNetwork != null
    }

}