package com.example.gifapp.data.db

import androidx.room.*

@Dao
interface GifDao {

    @Query("SELECT * FROM gif")
    fun getAll(): List<Gif>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg gifs: Gif)

    @Query("SELECT * FROM gif WHERE id=:gifId")
    fun findById(gifId: String): Gif

    @Delete
    fun delete(gif: Gif)
}