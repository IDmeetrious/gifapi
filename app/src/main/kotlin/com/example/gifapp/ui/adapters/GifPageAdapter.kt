package com.example.gifapp.ui.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.gifapp.R
import com.example.gifapp.data.db.Gif

class GifPageAdapter(private var data: List<Gif>, private val context: Context) :
    RecyclerView.Adapter<GifViewHolder>() {

    private var _positionLive = MutableLiveData<Int>()
    val positionLive: LiveData<Int>
        get() = _positionLive

    private var _gifId: MutableLiveData<String> = MutableLiveData()
    val gifId: LiveData<String>
        get() = _gifId

    private var counter = 0

    fun updateList(list: List<Gif>) {
        this.data = list
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_gif, parent, false)
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

        holder.image.setOnClickListener {
            counter += 1
            _positionLive.value = counter
            _gifId.value = item.id
        }
    }

    override fun getItemCount(): Int = data.size
}