package com.example.gifapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.gifapp.R
import com.example.gifapp.model.Gif
import com.example.gifapp.utils.Constants.GIF_DESC
import com.example.gifapp.utils.Constants.GIF_ID
import com.example.gifapp.utils.Constants.GIF_URI
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File

private const val TAG = "GifFullScreenFragment"

class GifFullScreenFragment : DialogFragment() {

    private lateinit var image: ImageView
    private lateinit var desc: TextView
    private lateinit var shareBtn: FloatingActionButton
    private var gif = Gif()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = layoutInflater.inflate(R.layout.fragment_gif_fullscreen, container, false)

        rootView.let {
            image = it.findViewById(R.id.fullscreen_iv)
            desc = it.findViewById(R.id.fullscreen_tv)
            shareBtn = it.findViewById(R.id.fullscreen_fab)
        }

        receiveGifBundle()
        loadImageView()
        initViews()

        return rootView
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    private fun initViews() {
        image.setOnClickListener {
            Log.i(TAG, "--> initViews: navigateUp")
            dismiss()
        }

        shareBtn.setOnClickListener {
            shareWith()
        }
    }

    private fun shareWith() {
        Log.i(TAG, "--> initRandom: Share with")
        Log.i(TAG, "--> initRandom: Uri[${gif.gifURL}]")
        Log.i(TAG, "--> initRandom: Text[${gif.description}]")
        val file =
            File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "${gif.id}.gif"
            )
        val uri = FileProvider.getUriForFile(requireContext(), "gifapp.fileprovider", file)
        val intent = Intent().apply {
            this.action = Intent.ACTION_SEND
            this.putExtra(Intent.EXTRA_TEXT, gif.description)
            this.putExtra(Intent.EXTRA_STREAM, uri)
            this.type = "image/gif"
            this.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        startActivity(intent)
    }

    private fun loadImageView() {
        Glide.with(requireContext())
            .asGif()
            .load(gif.gifURL)
            .into(image)
        desc.text = gif.description
    }

    private fun receiveGifBundle() {
        if (arguments != null) {
            gif.id = requireArguments().getString(GIF_ID, "")
            gif.description = requireArguments().getString(GIF_DESC, "")
            gif.gifURL = requireArguments().getString(GIF_URI, "")
        }
    }
}