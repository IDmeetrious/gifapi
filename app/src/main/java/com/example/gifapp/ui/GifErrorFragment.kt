package com.example.gifapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.gifapp.R
import com.example.gifapp.R.id.gifErrorFragment
import com.example.gifapp.network.NetworkUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class GifErrorFragment : Fragment() {
    private lateinit var tryBtn: FloatingActionButton
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gif_error, container, false)
        tryBtn = view.findViewById(R.id.error_refresh_fab)
        return view
    }

    override fun onStart() {
        super.onStart()

        tryBtn.setOnClickListener {
            if (NetworkUtil(requireContext()).onNetworkAvailable())
                view?.findNavController()!!.navigateUp()
            else
                Snackbar.make(
                    requireView(),
                    getString(R.string.no_internet_connection),
                    Snackbar.LENGTH_SHORT
                )
                    .show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            requireActivity().finish()
        }
    }
}