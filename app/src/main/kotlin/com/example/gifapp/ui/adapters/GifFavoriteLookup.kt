package com.example.gifapp.ui.adapters

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class GifFavoriteLookup(private val rv: RecyclerView) : ItemDetailsLookup<Long>() {
    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        rv.findChildViewUnder(e.x, e.y)
            ?.let {
                return (rv.getChildViewHolder(it) as GifFavoriteViewHolder).getItemDetails()
            } ?: return null
    }
}