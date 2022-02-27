package com.example.gifapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.gifapp.R
import com.example.gifapp.utils.network.NetworkUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class GifErrorFragment : Fragment() {
    private var tryBtn: FloatingActionButton? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_gif_error, container, false).apply {
            tryBtn = findViewById(R.id.error_refresh_fab)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.apply {
            onBackPressedDispatcher.addCallback(viewLifecycleOwner) { finish() }
        }

        tryBtn?.setOnClickListener {
            context?.let {
                if (NetworkUtil(it).isNetworkAvailable()) {
                    view.findNavController().navigateUp()
                } else {
                    Snackbar.make(view, getString(R.string.no_internet_connection), Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}