package com.example.gifapp.ui.adapters

import androidx.recyclerview.selection.ItemDetailsLookup
import com.example.gifapp.model.Gif

class GifDetails(private val adapterPosition: Int,
private val selectedKey: Gif?) : ItemDetailsLookup.ItemDetails<Gif>() {

    override fun getPosition() = adapterPosition
    override fun getSelectionKey() = selectedKey
}
