package com.example.gifapp.ui.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gifapp.R
import com.example.gifapp.model.Gif
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private const val TAG = "GifFavoriteAdapter"

class GifFavoriteAdapter(private var data: List<Gif>) : RecyclerView.Adapter<GifItemViewHolder>() {

    /** Created by ID
     * date: 17-May-21, 10:24 AM
     * TODO: change to StateFlow
     */
    private var _gif = MutableLiveData<Gif>()
    val gif: LiveData<Gif>
        get() = _gif

    private var _isSelected = MutableLiveData<Boolean>()
    val isSelected: LiveData<Boolean>
        get() = _isSelected

    private var _isEditable = MutableStateFlow(false)
    val isEditable: StateFlow<Boolean>
    get() = _isEditable

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

//        holder.isLongClick.observeForever {
//            Log.i(TAG, "--> onBindViewHolder: LongClick[$it]")
//            _isSelected.postValue(true)
//            _gif.postValue(item)
//
//            holder.selectBtn.visibility = View.VISIBLE
//            holder.itemView.scaleX = 0.9f
//            holder.itemView.scaleY = 0.9f
//        }
//
//        holder.isShortClick.observeForever {
//            Log.i(TAG, "--> onBindViewHolder: ShortClick[$it]")
//            _isSelected.postValue(false)
//            _gif.postValue(item)
//        }
        holder.itemView.setOnLongClickListener {
            if (!isEditable.value){
                _isEditable.value = true
                _isSelected.postValue(true)
                _gif.postValue(item)

                holder.selectBtn.visibility = View.VISIBLE
                holder.itemView.scaleY = 0.9f
                holder.itemView.scaleX = 0.9f
            }
            true
        }
        isSelected.observeForever {
            if (!it) {
                holder.selectBtn.visibility = View.GONE
                holder.imageView.refreshDrawableState()
            } else {
                holder.selectBtn.visibility = View.VISIBLE
                holder.itemView.scaleY = 0.9f
                holder.itemView.scaleX = 0.9f
            }
        }

        holder.itemView.setOnClickListener {

            if (isEditable.value){
                val state = _isSelected.value
                state?.let {
                    _isSelected.postValue(!state)
                }
            } else {
                _gif.postValue(item)
            }
        }

//        if (isSelected.value == true) {
//            holder.itemView.setOnClickListener {
//                Log.i(TAG, "--> onBindViewHolder: isDeSelected[${isSelected.value}]")
//                _isSelected.postValue(false)
//            }
//        } else {
//            holder.itemView.setOnClickListener {
//                Log.i(TAG, "--> onBindViewHolder: isSelected[${isSelected.value}]")
//                _isSelected.postValue(false)
//                _gif.postValue(item)
//
//                holder.selectBtn.visibility = View.GONE
//                holder.itemView.refreshDrawableState()
//
//            }
//        }

    }

    override fun getItemCount() = data.size

    fun clearSelection() {
        /** Created by ID
         * date: 17-May-21, 4:57 PM
         * TODO: clear selection on selected item
         */
        _isEditable.value = false
        _isSelected.postValue(false)
    }
}