package com.example.gifapp.ui.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.example.gifapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class GifFavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imageView: ImageView
    val description: TextView
    val selectBtn: FloatingActionButton


    init {
        itemView.let {
            imageView = it.findViewById(R.id.favorite_item_iv)
            description = it.findViewById(R.id.favorite_item_tv)
            selectBtn = it.findViewById(R.id.favorite_item_fab)
        }
    }

    fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> {
        return object : ItemDetailsLookup.ItemDetails<Long>() {
            override fun getPosition(): Int = absoluteAdapterPosition
            override fun getSelectionKey(): Long? = itemId

        }
    }

}
