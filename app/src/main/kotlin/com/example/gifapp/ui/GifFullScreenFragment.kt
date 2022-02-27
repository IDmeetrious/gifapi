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
import com.example.gifapp.data.FileRepository
import com.example.gifapp.model.Gif
import com.example.gifapp.utils.Constants.GIF_DESC
import com.example.gifapp.utils.Constants.GIF_ID
import com.example.gifapp.utils.Constants.GIF_URI
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File

private const val TAG = "GifFullScreenFragment"

class GifFullScreenFragment : DialogFragment() {

    private lateinit var image: ImageView
    private lateinit var desc: TextView
    private lateinit var shareBtn: FloatingActionButton
    private lateinit var deleteBtn: FloatingActionButton

    private var gif: Gif? = null
    private var counter = 0
    private lateinit var repository: FileRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = FileRepository.getInstance(requireContext())
        receiveGifBundle()
    }

    override fun onStart() {
        super.onStart()
        counter = repository.getFavoriteList().size

        Log.i(TAG, "onStart: counter=$counter")
        CoroutineScope(Dispatchers.Main).launch {
            repository.favoriteFlow.collect {
                Log.i(TAG, "onStart: favoriteFlow.size=${it}")
                if (it != counter)
                    dismiss()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = layoutInflater.inflate(R.layout.fragment_gif_fullscreen, container, false)

        rootView.let {
            image = it.findViewById(R.id.fullscreen_iv)
            desc = it.findViewById(R.id.fullscreen_tv)
            shareBtn = it.findViewById(R.id.fullscreen_share_fab)
            deleteBtn = it.findViewById(R.id.fullscreen_delete_fab)
        }

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

        deleteBtn.setOnClickListener {
            deleteFromFavorite()
        }
    }

    private fun deleteFromFavorite() {
        Log.i(TAG, "deleteFromFavorite: ")
        CoroutineScope(Dispatchers.IO).launch {
            gif?.let {
                repository.deleteById(it.id)
            }

        }
    }

    private fun shareWith() {
        gif?.let {
            Log.i(TAG, "--> initRandom: Share with")
            Log.i(TAG, "--> initRandom: Uri[${it.gifURL}]")
            Log.i(TAG, "--> initRandom: Text[${it.description}]")
            val file =
                File(
                    requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "${it.id}.gif"
                )
            val uri = FileProvider.getUriForFile(requireContext(), "gifapp.fileprovider", file)
            val intent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT, it.description)
                this.putExtra(Intent.EXTRA_STREAM, uri)
                this.type = "image/gif"
                this.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
            startActivity(intent)
        }

    }

    private fun loadImageView() {
        gif?.let {
            Glide.with(requireContext())
                .asGif()
                .load(it.gifURL)
                .into(image)
            desc.text = it.description
        }

    }

    private fun receiveGifBundle() {
        if (arguments != null) {
            Log.i(TAG, "--> receiveGifBundle: Arguments.NotNull")
            if (gif == null)
                gif = Gif(
                    id = requireArguments().getString(GIF_ID, ""),
                    description = requireArguments().getString(GIF_DESC, ""),
                    gifURL = requireArguments().getString(GIF_URI, "")
                )
            else gif?.apply {
                id = requireArguments().getString(GIF_ID, "")
                description = requireArguments().getString(GIF_DESC, "")
                gifURL = requireArguments().getString(GIF_URI, "")
            }

            Log.i(TAG, "--> receiveGifBundle: id=${gif?.id}")
            Log.i(TAG, "--> receiveGifBundle: description=${gif?.description}")
            Log.i(TAG, "--> receiveGifBundle: uri=${gif?.gifURL}")
        }
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop: ")
    }
}