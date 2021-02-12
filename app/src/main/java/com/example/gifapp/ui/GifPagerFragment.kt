package com.example.gifapp.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.gifapp.R
import kotlinx.coroutines.launch


class GifPagerFragment : Fragment(R.layout.fragment_gif_pager){
    private var pageIndex = 0

    private val viewModel: GifPagerViewModel by lazy {
        ViewModelProvider(this).get(GifPagerViewModel::class.java)
    }
    private lateinit var adapter: GifPagerAdapter
    private lateinit var viewPager: ViewPager2

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = view.findViewById(R.id.tab_viewpager)

        viewModel.latestList.observe(viewLifecycleOwner,{
            adapter = GifPagerAdapter(it)
            viewPager.adapter = adapter
        })

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                if (position == (viewPager.adapter?.itemCount?.minus(1))){
                    pageIndex++
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.getLatestGifs(pageIndex)
                    }
                }

                //TODO fix paging data
                if (position == 0) {
                    if (pageIndex > 0) pageIndex--
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.getLatestGifs(pageIndex)
                    }
                }
            }
        })
    }

}