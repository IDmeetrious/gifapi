package com.example.gifapp.ui.adapters

import androidx.recyclerview.selection.ItemDetailsLookup

interface ViewHolderWithDetails<TItem> {
    fun getItemDetail(): ItemDetailsLookup.ItemDetails<TItem>
}