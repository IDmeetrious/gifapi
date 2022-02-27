package com.example.gifapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Gif::class], version = 1)
abstract class GifDatabase : RoomDatabase() {

    abstract fun gifDao(): GifDao

    companion object {
        @Volatile
        private var instance: GifDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            GifDatabase::class.java,
            "gif_database"
        ).build()
    }
}