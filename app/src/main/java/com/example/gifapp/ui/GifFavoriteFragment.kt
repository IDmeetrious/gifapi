package com.example.gifapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gifapp.R
import com.example.gifapp.data.FileRepository
import com.example.gifapp.ui.adapters.GifFavoriteAdapter
import java.io.File

private const val TAG = "GifFavoriteFragment"

class GifFavoriteFragment: Fragment() {

    private lateinit var rv: RecyclerView
    private lateinit var adapter: GifFavoriteAdapter
    private lateinit var repository: FileRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_favorite, container, false)

        adapter = GifFavoriteAdapter(listOf())
        repository = FileRepository.getInstance(requireContext())

        rootView.let {
            rv = it.findViewById(R.id.favorite_rv)
        }


        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i(TAG, "--> onViewCreated: ")

        initViews()
    }

    private fun initViews() {
//        updateFavoriteList()
        rv.layoutManager = GridLayoutManager(requireContext(),3)
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