package com.example.gifapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gifapp.R
import com.example.gifapp.data.FileRepository
import com.example.gifapp.model.Gif
import com.example.gifapp.ui.adapters.GifFavoriteAdapter
import com.example.gifapp.utils.Constants.GIF_DESC
import com.example.gifapp.utils.Constants.GIF_ID
import com.example.gifapp.utils.Constants.GIF_URI

private const val TAG = "GifFavoriteFragment"

class GifFavoriteFragment : Fragment() {

    private lateinit var rv: RecyclerView
    private lateinit var adapter: GifFavoriteAdapter
    private lateinit var repository: FileRepository
    private var gif = Gif()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_favorite, container, false)

        adapter = GifFavoriteAdapter(listOf())
        adapter.gif.observe(viewLifecycleOwner, { mGif ->
            gif = mGif
            val args = Bundle().apply {
                Log.i(TAG, "--> onCreateView: id[${gif.id}]")
                this.putString(GIF_ID, gif.id)
                Log.i(TAG, "--> onCreateView: description[${gif.description}]")
                this.putString(GIF_DESC, gif.description)
                Log.i(TAG, "--> onCreateView: uri[${gif.gifURL}]")
                this.putString(GIF_URI, gif.gifURL)
            }

            Log.i(TAG, "--> onCreateView: moveToFullScreen")
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.nav_host_fragment, GifFullScreenFragment::class.java, arguments, TAG)
//                .commit()
            val fragment = GifFullScreenFragment()
            fragment.arguments = args
            fragment.show(childFragmentManager, "GifFullScreenFragment")
        })
        repository = FileRepository.getInstance(requireContext())

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i(TAG, "--> onViewCreated: ")

        initViews(view)
    }

    private fun initViews(view: View) {
        rv = view.findViewById(R.id.favorite_rv)
        rv.layoutManager = GridLayoutManager(requireContext(), 3)
        rv.adapter = adapter
    }

    private fun updateFavoriteList() {
        val list = repository.loadFromStorage()
        Log.i(TAG, "--> updateFavoriteList: ${list.size}")
        adapter.updateData(list)
    }

    override fun onResume() {
        super.onResume()
        updateFavoriteList()
    }
}