package com.example.gifapp.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gif")
data class Gif(
    @PrimaryKey
    var id: String = "",
    var description: String = "",
    var gifURL: String = ""
)
