package com.example.gifapp.ui.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gifapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class GifItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val imageView: ImageView
    val description: TextView
    val selectBtn: FloatingActionButton
//    private var _isLongClick = MutableLiveData<Boolean>()
//    val isLongClick: LiveData<Boolean>
//    get() = _isLongClick
//    private var _isShortClick = MutableLiveData<Boolean>()
//    val isShortClick: LiveData<Boolean>
//        get() = _isShortClick

    init {
        itemView.let {
            imageView = it.findViewById(R.id.favorite_item_iv)
            description = it.findViewById(R.id.favorite_item_tv)
            selectBtn = it.findViewById(R.id.favorite_item_fab)
        }

//        itemView.setOnLongClickListener {
//            _isLongClick.postValue(true)
//            true
//        }
//        itemView.setOnClickListener {
//            _isShortClick.postValue(true)
//        }
    }
}
