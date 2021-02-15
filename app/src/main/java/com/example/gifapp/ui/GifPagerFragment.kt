package com.example.gifapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
    private val viewModel: GifPagerViewModel by lazy {
        ViewModelProvider(this).get(GifPagerViewModel::class.java)
    }
    private lateinit var adapter: GifPagerAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var prevFab: FloatingActionButton
    private lateinit var nextFab: FloatingActionButton
    private lateinit var tabLayout: TabLayout

    private var currentPosition = 0
    private var pageIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = GifPagerAdapter(viewModel.data.value ?: mutableListOf())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            viewPager = findViewById(R.id.tab_viewpager)
            tabLayout = findViewById(R.id.tab_layout)
            prevFab = findViewById(R.id.tab_prev_fab)
            nextFab = findViewById(R.id.tab_next_fab)
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    getString(R.string.latest) -> setPageLatest()
                    getString(R.string.top) -> setPageTop()
                    getString(R.string.random) -> {
                        setPageRandom()
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

        onPrevButton()

        //TODO fix change page on random
        nextFab.setOnClickListener {
            currentPosition++
            getRandom()
        }
    }

    private fun onPrevButton() {
        prevFab.setOnClickListener {
            if (currentPosition > 0)
                currentPosition = viewPager.currentItem - 1
            viewPager.setCurrentItem(currentPosition, false)
        }
    }

    private fun updateUI() {
        viewModel.data.observe(viewLifecycleOwner, {
            adapter = GifPagerAdapter(it)
            adapter.notifyDataSetChanged()
            viewPager.adapter = adapter

            //Set current page after click or slide
            viewPager.setCurrentItem(currentPosition, false)
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updateStatePrevBtn()
                }
            })
        })
    }

    private fun getRandom() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getRandom()
        }
    }

    private fun getLatest(page: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getLatest(page)
        }
    }

    private fun getTop(page: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getTop(page)
        }
    }

    private fun setPageRandom() {
        getRandom()
        Toast.makeText(context, "Random", Toast.LENGTH_SHORT).show()
    }

    private fun setPageTop() {
        currentPosition = 0
        getTop(pageIndex)
        Toast.makeText(context, "Top", Toast.LENGTH_SHORT).show()

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateStatePrevBtn()
                currentPosition = position


                if (position == viewPager.adapter?.itemCount?.minus(1)) {
                    pageIndex++
                    getTop(pageIndex)
                }
            }
        })
    }

    private fun setPageLatest() {
        currentPosition = 0
        getLatest(pageIndex)
        Toast.makeText(context, "Latest", Toast.LENGTH_SHORT).show()

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateStatePrevBtn()
                currentPosition = position

                if (position == viewPager.adapter?.itemCount?.minus(1)) {
                    pageIndex++
                    getLatest(pageIndex)
                }
            }
        })
    }

    private fun updateStatePrevBtn() {
        prevFab.isEnabled = viewPager.currentItem > 0
    }

    override fun onDetach() {
        super.onDetach()
        viewLifecycleOwner.lifecycleScope.launch {
            Glide.get(requireContext()).clearDiskCache()
        }
    }

}