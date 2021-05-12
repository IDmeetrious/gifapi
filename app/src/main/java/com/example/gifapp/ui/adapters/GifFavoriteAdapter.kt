package com.example.gifapp.ui.adapters

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gifapp.R
import com.example.gifapp.model.Gif

private const val TAG = "GifFavoriteAdapter"

class GifFavoriteAdapter(private var data: List<Gif>) : RecyclerView.Adapter<GifItemViewHolder>() {

    private var _gif = MutableLiveData<Gif>()
    val gif: LiveData<Gif>
        get() = _gif

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

        Glide.with(holder.itemView)
            .asGif()
            .load(Uri.parse(item.gifURL))
            .into(holder.imageView)

        holder.description.text = item.description

        holder.imageView.adjustViewBounds = true
        holder.imageView.setOnClickListener {
            Log.i(TAG, "--> onBindViewHolder: ${item.id}")
            _gif.postValue(item)

        }
        holder.imageView.setOnLongClickListener {
            Log.i(TAG, "--> onBindViewHolder: onLongClick")
            holder.selectBtn.visibility = View.VISIBLE
            holder.itemView.scaleX = 0.9f
            holder.itemView.scaleY = 0.9f
            true
        }

    }

    override fun getItemCount() = data.size

}