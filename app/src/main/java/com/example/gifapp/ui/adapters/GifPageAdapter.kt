package com.example.gifapp.ui.adapters

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.gifapp.R
import com.example.gifapp.model.Gif
import com.example.gifapp.ui.GifViewHolder

private const val TAG = "GifPageAdapter"

class GifPageAdapter(private val data: MutableList<Gif>) : RecyclerView.Adapter<GifViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_gif, parent, false)
        return GifViewHolder(view)
    }

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        val item = data[position]
        if (item.gifURL.isNotEmpty() && item.gifURL.isNotBlank()) {
            Glide.with(holder.itemView)
                .load(item.gifURL)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        //TODO set error page for empty url
                        holder.image.setBackgroundColor(Color.BLACK)
                        holder.description.text = "Can't load image.. Try again!"
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.progressBar.isVisible = false
                        holder.description.text = item.description
                        return false
                    }
                })
                .fitCenter()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image)
        }

    }

    override fun getItemCount(): Int = data.size
}