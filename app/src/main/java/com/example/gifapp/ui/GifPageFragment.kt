package com.example.gifapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

private const val TAG = "GifPageFragment"

class GifPageFragment : Fragment() {
    private val viewModel: GifPageViewModel by lazy {
        ViewModelProvider(this).get(GifPageViewModel::class.java)
    }
    private lateinit var adapter: GifPageAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var likeBtn: FloatingActionButton
    private lateinit var shareBtn: FloatingActionButton
    private var currentPosition = 0
    private var gif = Gif()

    private lateinit var repository: FileRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.i(TAG, "--> onCreateView: ")
        val view = layoutInflater.inflate(R.layout.fragment_gif_page, container, false)
        adapter = GifPageAdapter(listOf(), requireContext())
        repository = FileRepository.getInstance(requireContext())

        Log.i(TAG, "--> onCreateView: pos[${adapter.positionLive.value}]")

        adapter.positionLive.observe(viewLifecycleOwner, {
            Log.i(TAG, "--> onCreateView: position[$it]")
            it?.let {
                currentPosition = it
                getRandom()
            }
        })
        viewModel.gifLive.observe(viewLifecycleOwner, {
            Log.i(TAG, "--> onCreateView: GifId[$it]")
            gif = it
        })

        view.apply {
            viewPager = findViewById(R.id.page_viewpager)
            likeBtn = findViewById(R.id.page_like)
            shareBtn = findViewById(R.id.fullscreen_share_fab)
        }
        viewPager.isNestedScrollingEnabled = false

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i(TAG, "--> onViewCreated: ")
        initRandom()
        updateUI()
    }

    private fun updateUI() {

        viewModel.data.observe(viewLifecycleOwner, { list ->
            adapter.updateList(list)

            viewPager.adapter = adapter
            viewPager.setCurrentItem(currentPosition, false)
        })

        viewModel.likeBtnState.observe(viewLifecycleOwner, {
            Log.i(TAG, "--> updateUI: $it")
            if (it) {
                likeBtn.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_favorite_24
                    )
                )
            } else {
                likeBtn.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_favorite_empty_24
                    )
                )
            }
        })
    }

    private fun getRandom() {
        Log.i(TAG, "--> getRandom: ")

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getRandom()
            viewModel.setLikeBtnState(false)
        }
    }

    //*** Random page is Done! ***
    private fun initRandom() {
        getRandom()

        shareBtn.setOnClickListener {
            shareWith()
        }


        likeBtn.setOnClickListener {
//            viewModel.updateLikeBtnState()
            viewModel.let {
                if (it.likeBtnState.value == false) {
                    Log.i(TAG, "--> initRandom: Add to favorites")
                    it.setLikeBtnState(true)
                    viewModel.addFavorite(gif)
                } else {
                    Log.i(TAG, "initRandom: Delete from favorite")
                    it.setLikeBtnState(false)
                    viewModel.deleteFavorite(gif)
                }

            }
//
//            if (favoriteCounter == 0) repository.addToFavorite(gif)
//            favoriteCounter++

            Log.i(TAG, "--> initRandom: State[${viewModel.likeBtnState.value}]")
        }
    }

    private fun shareWith() {
        Log.i(TAG, "--> initRandom: Share with")
        val file =
            File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "${gif.id}.gif"
            )
        val uri = FileProvider.getUriForFile(requireContext(), "gifapp.fileprovider", file)
        Log.i(TAG, "--> initRandom: Uri[$uri]")
        val description = gif.description
        Log.i(TAG, "--> initRandom: Text[$description]")
        val intent = Intent().apply {
            this.action = Intent.ACTION_SEND
            this.putExtra(Intent.EXTRA_TEXT, description)
            this.putExtra(Intent.EXTRA_STREAM, uri)
            this.type = "image/gif"
            this.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "--> onResume: ")

        // Refresh like button state due availability in favorite list
        viewModel.setLikeBtnState(viewModel.getFavorite(gif))
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "--> onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "--> onStop: ")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(TAG, "--> onDestroyView: ")
    }

    override fun onDestroy() {
        Log.i(TAG, "--> onDestroy: ")
//        CoroutineScope(Dispatchers.IO).launch{
//            repository.clearStorage()
//        }
        viewModel.clearRepository()
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
        Log.i(TAG, "--> onDetach: ")
    }

}