package com.example.gifapp.ui.adapters

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.gifapp.utils.Constants.ARGS_CATEGORY
import com.example.gifapp.utils.Constants.TOTAL_PAGES
import com.example.gifapp.ui.GifCategoriesFragment
import com.example.gifapp.ui.GifFavoriteFragment
import com.example.gifapp.ui.GifPageFragment

private const val TAG = "GifCategoriesAdapter"
class GifCategoriesAdapter(fr: GifCategoriesFragment) : FragmentStateAdapter(fr) {
    override fun getItemCount(): Int = TOTAL_PAGES

    override fun createFragment(position: Int): Fragment {
        val fragment = Fragment()
//        val category: String = when (position) {
//            0 -> "Random"
//            1 -> "Favorite"
//            else -> "Item"
//        }
//        fragment.arguments = Bundle().apply {
//            putSerializable(ARGS_CATEGORY, category)
//        }
//        Log.d(TAG, "--> createFragment: $category")
//        return fragment
        when(position){
            0 -> return GifPageFragment()
            1 -> return GifFavoriteFragment()
        }
        return fragment
    }
}