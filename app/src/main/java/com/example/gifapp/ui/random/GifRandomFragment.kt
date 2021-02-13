package com.example.gifapp.ui.random

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.gifapp.R
import com.example.gifapp.ui.GifPagerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

private const val TAG = "GifRandomFragment"

class GifRandomFragment : Fragment(R.layout.fragment_gif_random) {
    private var pageIndex = 0

    private val viewModel: GifRandomViewModel by lazy {
        ViewModelProvider(this).get(GifRandomViewModel::class.java)
    }
    private lateinit var adapter: GifPagerAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var prevFab: FloatingActionButton
    private lateinit var nextFab: FloatingActionButton

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = GifPagerAdapter(viewModel.data.value ?: mutableListOf())

        viewPager = view.findViewById(R.id.tab_viewpager)
        prevFab = view.findViewById(R.id.tab_prev_fab)
        nextFab = view.findViewById(R.id.tab_next_fab)

        updateUI()

        prevFab.setOnClickListener {
            if (pageIndex > 0)
                pageIndex = viewPager.currentItem - 1
            viewPager.setCurrentItem(pageIndex, false)
        }
        nextFab.setOnClickListener {
            pageIndex = viewPager.currentItem + 1
            getRandom()
        }

    }

    private fun updateUI() {
        viewModel.data.observe(viewLifecycleOwner, {
            adapter = GifPagerAdapter(it)
            adapter.notifyDataSetChanged()
            viewPager.adapter = adapter

            //Set current page after click or slide
            viewPager.setCurrentItem(pageIndex, false)
        })
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                changeStatePrevBtn()
            }
        })
    }

    private fun getRandom() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getRandom()
        }
    }

    private fun changeStatePrevBtn() {
        prevFab.isEnabled = viewPager.currentItem > 0
    }

    override fun onDetach() {
        super.onDetach()
        viewLifecycleOwner.lifecycleScope.launch {
            Glide.get(requireContext()).clearDiskCache()
        }
    }

}