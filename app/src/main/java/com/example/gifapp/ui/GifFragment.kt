package com.example.gifapp.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.gifapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

private const val TAG = "GifFragment"

class GifFragment : Fragment(R.layout.fragment_gif) {

    private val viewModel = GifViewModel()
    private lateinit var gifIv: ImageView
    private lateinit var descTv: TextView
    private lateinit var refreshFab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Load random gif
        viewModel.getRandom()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.apply {
            gifIv = findViewById(R.id.gif_iv)
            descTv = findViewById(R.id.gif_desc_tv)
            refreshFab = findViewById(R.id.gif_refresh_fab)
        }

        viewModel.randomGif.observe(viewLifecycleOwner, {
            Glide.with(requireContext())
                .load(it.gifURL)
                .centerCrop()
                .into(gifIv)
            descTv.text = it.description
        })

        refreshFab.setOnClickListener {
            viewModel.getRandom()
        }
    }
}