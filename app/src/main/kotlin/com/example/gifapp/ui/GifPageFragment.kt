package com.example.gifapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.gifapp.R
import com.example.gifapp.data.FileRepository
import com.example.gifapp.model.Gif
import com.example.gifapp.ui.adapters.GifPageAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File

class GifPageFragment : Fragment() {
    private val viewModel: GifPageViewModel by lazy {
        ViewModelProvider(this)[GifPageViewModel::class.java]
    }
    private lateinit var adapter: GifPageAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var likeBtn: FloatingActionButton
    private lateinit var shareBtn: FloatingActionButton
    private var currentPosition = 0
    private var gif = Gif()

    private lateinit var repository: FileRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = layoutInflater.inflate(R.layout.fragment_gif_page, container, false)

        adapter = GifPageAdapter(listOf(), requireContext())
        repository = FileRepository.getInstance(requireContext())

        adapter.positionLive.observe(viewLifecycleOwner) {
            it?.let {
                currentPosition = it
                getRandom()
            }
        }
        viewModel.gifLive.observe(viewLifecycleOwner) {
            gif = it
        }

        with(rootView) {
            viewPager = findViewById(R.id.page_viewpager)
            likeBtn = findViewById(R.id.page_like)
            shareBtn = findViewById(R.id.fullscreen_share_fab)
        }
        viewPager.isNestedScrollingEnabled = false

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRandom()
        updateUI()
    }

    private fun updateUI() {

        viewModel.data.observe(viewLifecycleOwner) { list ->
            adapter.updateList(list)

            viewPager.adapter = adapter
            viewPager.setCurrentItem(currentPosition, false)
        }

        viewModel.likeBtnState.observe(viewLifecycleOwner) {
            val drawable =
                if (it) R.drawable.ic_favorite_24
                else R.drawable.ic_favorite_empty_24

            likeBtn.setImageDrawable(ContextCompat.getDrawable(requireContext(), drawable))
        }
    }

    private fun getRandom() {
        viewModel.getRandom()
        viewModel.setLikeBtnState(false)
    }

    private fun initRandom() {
        getRandom()

        shareBtn.setOnClickListener { shareWith() }

        likeBtn.setOnClickListener {
            viewModel.let {
                if (it.likeBtnState.value == false) {
                    it.setLikeBtnState(true)
                    it.addFavorite(gif)
                } else {
                    it.setLikeBtnState(false)
                    it.deleteFavorite(gif)
                }
            }
        }
    }

    private fun shareWith() {
        context?.let {
            val file = File(it.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "${gif.id}.gif")
            val uri = FileProvider.getUriForFile(it, AUTHORITY, file)
            val description = gif.description

            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "video/*"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, description)
            }
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh like button state due availability in favorite list
        viewModel.setLikeBtnState(viewModel.getFavorite(gif))
    }

    override fun onDestroy() {
        viewModel.clearRepository()
        super.onDestroy()
    }

    companion object {
        private const val AUTHORITY = "com.example.gifapp.fileprovider"
    }
}