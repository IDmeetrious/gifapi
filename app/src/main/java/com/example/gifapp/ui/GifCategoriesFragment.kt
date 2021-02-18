package com.example.gifapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.gifapp.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

private const val TAG = "GifCategoriesFragment"

class GifCategoriesFragment : Fragment(R.layout.fragment_gif_categories) {

    private lateinit var categoriesAdapter: GifCategoriesAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabs: TabLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager = view.findViewById(R.id.tab_viewpager)
        tabs = view.findViewById(R.id.tab_layout)
        categoriesAdapter = GifCategoriesAdapter(this)
        viewPager.adapter = categoriesAdapter

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            val tabItem = when (position) {
                0 -> getString(R.string.random)
                1 -> getString(R.string.latest)
                2 -> getString(R.string.top)
                else -> "Item"
            }
            tab.text = tabItem
            Log.d(TAG, "--> onTabCreated: ${tab.text}")
        }.attach()
    }
    override fun onDetach() {
        super.onDetach()
        viewLifecycleOwner.lifecycleScope.launch {
            Glide.get(requireContext()).clearDiskCache()
        }
    }

}