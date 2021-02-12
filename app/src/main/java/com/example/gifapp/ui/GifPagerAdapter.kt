package com.example.gifapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.gifapp.R
import com.example.gifapp.model.Gif

class GifPagerAdapter(private val data: List<Gif>) : RecyclerView.Adapter<GifViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_gif_random, parent, false)
        return GifViewHolder(view)
    }

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        val item = data[position]
        if (item.gifURL.isNotBlank() && item.gifURL.isNotEmpty()) {
            Glide.with(holder.itemView)
                .load(item.gifURL)
                .fitCenter()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image)
            holder.description.text = item.description
        }
    }

    override fun getItemCount(): Int = data.size
}