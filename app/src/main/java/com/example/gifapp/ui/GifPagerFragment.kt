package com.example.gifapp.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.gifapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

private const val TAG = "GifPagerFragment"

class GifPagerFragment : Fragment(R.layout.fragment_gif_pager) {
    private var pageIndex = 0

    private val viewModel: GifPagerViewModel by lazy {
        ViewModelProvider(this).get(GifPagerViewModel::class.java)
    }
    private lateinit var adapter: GifPagerAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var prevFab: FloatingActionButton
    private lateinit var nextFab: FloatingActionButton

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = view.findViewById(R.id.tab_viewpager)
        tabLayout = view.findViewById(R.id.tab_layout)
        prevFab = view.findViewById(R.id.tab_prev_fab)
        nextFab = view.findViewById(R.id.tab_next_fab)

        setPrevFab(false)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    getString(R.string.latest) -> getLatest()
                    getString(R.string.best) -> getBest()
                    getString(R.string.random) -> {
                        getRandom()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Log.d(TAG, "onTabUnselected: ${tab?.text}")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                Log.d(TAG, "onTabReselected: ${tab?.text}")
            }

        })
        updateUI()

        if (tabLayout.selectedTabPosition == 1) {
            nextFab.setOnClickListener {
                getRandom()
            }
        }

    }

    private fun updateUI() {

        viewModel.data.observe(viewLifecycleOwner, {
            adapter = GifPagerAdapter(it)
            adapter.notifyDataSetChanged()
            viewPager.adapter = adapter
            if (it.size > 1) setPrevFab(true)
        })
    }

    private fun getRandom() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getRandom()
        }
    }

    private fun getBest() {
        Log.d(TAG, "onTabSelected: Best")
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getBest()
        }
    }

    private fun getLatest() {
        Log.d(TAG, "onTabSelected: Latest")
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getLatest()
        }
    }

    private fun setPrevFab(enabled: Boolean) {
        prevFab.isEnabled = enabled
    }

    override fun onDetach() {
        super.onDetach()
        viewLifecycleOwner.lifecycleScope.launch {
            Glide.get(requireContext()).clearDiskCache()
        }
    }

}