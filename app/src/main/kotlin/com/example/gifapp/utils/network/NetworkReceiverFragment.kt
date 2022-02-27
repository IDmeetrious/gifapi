package com.example.gifapp.utils.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gifapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class NetworkReceiverFragment : Fragment() {
    private lateinit var cm: ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onLost(network: Network) {
            super.onLost(network)
            CoroutineScope(Dispatchers.Main).launch {
                findNavController().navigate(R.id.action_gifCategoriesFragment_to_gifErrorFragment)
            }
        }

        override fun onUnavailable() {
            super.onUnavailable()
            CoroutineScope(Dispatchers.Main).launch {
                findNavController().navigate(R.id.action_gifCategoriesFragment_to_gifErrorFragment)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cm = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    override fun onStart() {
        super.onStart()
        cm.registerNetworkCallback(NetworkRequest.Builder().build(), networkCallback)
    }

    override fun onStop() {
        super.onStop()
        cm.unregisterNetworkCallback(networkCallback)
    }
}