package com.example.gifapp.ui.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.gifapp.R
import com.example.gifapp.model.Gif

class GifFavoriteAdapter(private var data: List<Gif>) : RecyclerView.Adapter<GifItemViewHolder>() {

    fun updateData(list: List<Gif>) {
        this.data = list
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_gif_favorite_item, parent, false)
        return GifItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GifItemViewHolder, position: Int) {
        val item = data[position]

        holder.description.text = item.description
        holder.imageView.setImageURI(Uri.parse(item.gifURL))
    }

    override fun getItemCount() = data.size

}