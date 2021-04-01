package com.example.gifapp.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast

private const val TAG = "NetworkUtil"

class NetworkUtil(private var context: Context) {

    fun onNetworkAvailable(): Boolean {
        val cm: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm.activeNetwork
        } else {
            cm.activeNetworkInfo
        }
        return if (activeNetwork != null) {
            Log.i(TAG, "--> onActiveNetwork: Network is On!")
            true
        } else {
            Log.e(TAG, "--> onActiveNetwork: No internet yet")
            false
        }
    }

}