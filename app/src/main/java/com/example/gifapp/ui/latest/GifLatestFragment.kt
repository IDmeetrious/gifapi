package com.example.gifapp.ui.latest

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gifapp.R

private const val TAG = "GifFragment"

class GifLatestFragment : Fragment(R.layout.fragment_gif) {

    private val viewModel: GifLatestViewModel by lazy {
        ViewModelProvider(this).get(GifLatestViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}