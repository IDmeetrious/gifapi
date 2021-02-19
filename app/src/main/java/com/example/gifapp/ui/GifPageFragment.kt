package com.example.gifapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.gifapp.R
import com.example.gifapp.other.Constants
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

private const val TAG = "GifPagerFragment"

class GifPageFragment : Fragment() {
    private val viewModel: GifPageViewModel by lazy {
        ViewModelProvider(this).get(GifPageViewModel::class.java)
    }
    private lateinit var adapter: GifPageAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var prevBtn: FloatingActionButton
    private lateinit var nextBtn: FloatingActionButton
    private var currentPosition = 0
    private var pageIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = layoutInflater.inflate(R.layout.fragment_gif_page, container, false)
        adapter = GifPageAdapter(mutableListOf())
        view.apply {
            viewPager = findViewById(R.id.page_viewpager)
            prevBtn = findViewById(R.id.page_prev_fab)
            nextBtn = findViewById(R.id.page_next_fab)
        }
        viewPager.isHorizontalScrollBarEnabled = false
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (arguments?.containsKey(Constants.ARGS_CATEGORY) != null) {
            arguments?.apply {
                val category = this.getSerializable(Constants.ARGS_CATEGORY)
                when (category) {
                    getString(R.string.random) -> {
                        Log.i(TAG, "--> onCreateView: args = $category")
                        initRandom()
                    }
                    getString(R.string.latest) -> {
                        Log.i(TAG, "--> onCreateView: args = $category")
                        initLatest()
                    }
                    getString(R.string.top) -> {
                        Log.i(TAG, "--> onCreateView: args = $category")
                        initTop()
                    }
                }
            }
        }
        updateUI()
    }

    private fun updateUI() {
        viewModel.data.observe(viewLifecycleOwner, {
            adapter = GifPageAdapter(it)
            adapter.notifyDataSetChanged()
            viewPager.adapter = adapter
            viewPager.setCurrentItem(currentPosition, false)
        })
        viewModel.prevBtnState.observe(viewLifecycleOwner,{
            prevBtn.isEnabled = it
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


    //*** Random page is Done! ***
    private fun initRandom() {
        adapter = GifPageAdapter(mutableListOf())
        getRandom()

        nextBtn.setOnClickListener {
            currentPosition++
            if (currentPosition > 0) viewModel.setButtonState(true)
            Toast.makeText(context, "onNext: Random", Toast.LENGTH_SHORT).show()
            Log.i(TAG, "initRandom: currentPosition --> $currentPosition")
            getRandom()
        }
        prevBtn.setOnClickListener {
            if (currentPosition > 0) currentPosition--
            if (currentPosition == 0) viewModel.setButtonState(false)
            viewPager.setCurrentItem(currentPosition, false)
            Toast.makeText(context, "onPrev: Random", Toast.LENGTH_SHORT).show()
            Log.i(TAG, "initRandom: currentPosition --> $currentPosition")
        }
    }

    //TODO init getting top gifs
    private fun initTop() {
        adapter = GifPageAdapter(mutableListOf())
        getTop(pageIndex)

        nextBtn.setOnClickListener {
            Toast.makeText(context, "onNext: Top", Toast.LENGTH_SHORT).show()
        }
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentPosition = position
                if (position == viewPager.adapter?.itemCount?.minus(1)) {
                    pageIndex++
                    getTop(pageIndex)
                }
            }
        })
    }

    //TODO init getting latest gifs
    private fun initLatest() {
        adapter = GifPageAdapter(mutableListOf())
        currentPosition = 0
        getLatest(pageIndex)

        nextBtn.setOnClickListener {
            Toast.makeText(context, "onNext: Latest", Toast.LENGTH_SHORT).show()
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentPosition = position

                if (position == viewPager.adapter?.itemCount?.minus(1)) {
                    pageIndex++
                    getLatest(pageIndex)
                }
            }
        })
    }
}