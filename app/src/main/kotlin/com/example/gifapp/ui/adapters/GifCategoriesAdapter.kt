package com.example.gifapp.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.gifapp.ui.GifCategoriesFragment
import com.example.gifapp.ui.GifFavoriteFragment
import com.example.gifapp.ui.GifPageFragment

class GifCategoriesAdapter(fr: GifCategoriesFragment) : FragmentStateAdapter(fr) {
    override fun getItemCount(): Int = TOTAL_PAGES

    override fun createFragment(position: Int): Fragment {
        val fragment = Fragment()
        when (position) {
            0 -> return GifPageFragment()
            1 -> return GifFavoriteFragment()
        }
        return fragment
    }

    companion object {
        private const val TOTAL_PAGES = 2
    }
}