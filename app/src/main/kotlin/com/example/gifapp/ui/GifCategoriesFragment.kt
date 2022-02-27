package com.example.gifapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.gifapp.R
import com.example.gifapp.ui.adapters.GifCategoriesAdapter
import com.example.gifapp.utils.network.NetworkReceiverFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GifCategoriesFragment : NetworkReceiverFragment() {

    private lateinit var categoriesAdapter: GifCategoriesAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabs: TabLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_gif_categories, container, false).apply {
            viewPager = findViewById(R.id.tab_viewpager)
            tabs = findViewById(R.id.tab_layout)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        categoriesAdapter = GifCategoriesAdapter(this)
        viewPager.adapter = categoriesAdapter

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            val tabItem = when (position) {
                0 -> getString(R.string.random)
                1 -> getString(R.string.favorite)
                else -> "Item"
            }
            tab.text = tabItem
        }.attach()
    }

    override fun onDetach() {
        super.onDetach()
        CoroutineScope(Dispatchers.IO).launch {
            context?.let {
                Glide.get(it).clearDiskCache()
            }
        }
    }
}