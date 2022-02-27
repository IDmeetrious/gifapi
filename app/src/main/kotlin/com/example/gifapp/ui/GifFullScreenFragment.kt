package com.example.gifapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.gifapp.R
import com.example.gifapp.data.FileRepository
import com.example.gifapp.data.db.Gif
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File

class GifFullScreenFragment : DialogFragment() {

    private lateinit var image: ImageView
    private lateinit var desc: TextView
    private lateinit var shareBtn: FloatingActionButton
    private lateinit var deleteBtn: FloatingActionButton

    private var gif: Gif? = null
    private var counter = 0
    private lateinit var repository: FileRepository

    override fun getTheme(): Int = R.style.DialogTheme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = FileRepository.getInstance(requireContext())
        receiveGifBundle()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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

    override fun onStart() {
        super.onStart()
        counter = repository.getFavoriteList().size

        lifecycleScope.launch {
            repository.favoriteFlow.collect {
                if (it != counter) dismiss()
            }
        }
    }

    private fun initViews() {
        image.setOnClickListener { dismiss() }
        shareBtn.setOnClickListener { shareWith() }
        deleteBtn.setOnClickListener { deleteFromFavorite() }
    }

    private fun deleteFromFavorite() {
        lifecycleScope.launch {
            gif?.let {
                repository.deleteById(it.id)
            }
        }
    }

    private fun shareWith() {
        context?.let {
            gif?.let { gif ->
                val file = File(it.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "${gif.id}.gif")
                val uri = FileProvider.getUriForFile(it, AUTHORITY, file)

                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "video/*"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    putExtra(Intent.EXTRA_TEXT, gif.description)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                startActivity(intent)
            }
        }
    }

    private fun loadImageView() {
        context?.let { context ->
            gif?.let {
                Glide.with(context)
                    .asGif()
                    .load(it.gifURL)
                    .into(image)
                desc.text = it.description
            }
        }
    }

    private fun receiveGifBundle() {
        arguments?.let {
            if (gif == null)
                gif = Gif(
                    id = it.getString(GIF_ID, ""),
                    description = it.getString(GIF_DESC, ""),
                    gifURL = it.getString(GIF_URI, "")
                )
            else gif?.apply {
                id = it.getString(GIF_ID, "")
                description = it.getString(GIF_DESC, "")
                gifURL = it.getString(GIF_URI, "")
            }
        }
    }

    companion object {
        private const val AUTHORITY = "com.example.gifapp.fileprovider"
        private const val GIF_ID = "gif_id"
        private const val GIF_DESC = "gif_desc"
        private const val GIF_URI = "gif_uri"
    }
}