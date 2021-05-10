package com.example.gifapp.ui.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gifapp.R

class GifItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val imageView: ImageView
    val description: TextView

    init {
        itemView.let {
            imageView = it.findViewById(R.id.favorite_item_iv)
            description = it.findViewById(R.id.favorite_item_tv)
        }
    }
}
