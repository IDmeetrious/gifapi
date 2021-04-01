package com.example.gifapp.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.gifapp.R

private const val TAG = "NetworkFragment"

abstract class NetworkReceiverFragment : Fragment() {
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            val connection =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    connection.activeNetwork
                } else {
                    connection.activeNetworkInfo
                }
            if (networkInfo != null) {
                Log.i(TAG, "--> onReceive: Network: ON")
            } else {
                Log.e(TAG, "--> onReceive: Network: OFF")
                findNavController().navigate(R.id.action_gifCategoriesFragment_to_gifErrorFragment)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        requireActivity().registerReceiver(receiver, filter)
    }

    override fun onStop() {
        super.onStop()
        requireActivity().unregisterReceiver(receiver)
    }
}