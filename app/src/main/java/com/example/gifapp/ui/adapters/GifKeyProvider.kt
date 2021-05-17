package com.example.gifapp.ui.adapters

import androidx.recyclerview.selection.ItemKeyProvider
import com.example.gifapp.model.Gif

class GifKeyProvider(private val items: List<Gif>) :
    ItemKeyProvider<Gif>(SCOPE_CACHED) {

    override fun getKey(position: Int) = items.getOrNull(position)
    override fun getPosition(key: Gif) = items.indexOf(key)
}