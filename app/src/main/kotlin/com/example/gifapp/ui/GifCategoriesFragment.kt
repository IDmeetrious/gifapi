package com.example.gifapp.ui

import android.os.Bundle
import android.util.Log
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

private const val TAG = "GifCategoriesFragment"

class GifCategoriesFragment : NetworkReceiverFragment() {

    private lateinit var categoriesAdapter: GifCategoriesAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabs: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mView = inflater.inflate(R.layout.fragment_gif_categories, container, false)
        mView.apply {
            viewPager = findViewById(R.id.tab_viewpager)
            tabs = findViewById(R.id.tab_layout)
        }
        return mView
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
            Log.i(TAG, "--> onTabCreated: ${tab.text}")
        }.attach()

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.i(TAG, "--> onTabSelected: ${tab?.text}")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Log.i(TAG, "--> onTabUnselected: ${tab?.text}")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                Log.i(TAG, "--> onTabReselected: ${tab?.text}")
            }
        })
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