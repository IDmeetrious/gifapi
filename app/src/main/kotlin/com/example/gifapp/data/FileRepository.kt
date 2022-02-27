package com.example.gifapp.data

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.example.gifapp.data.db.GifDatabase
import com.example.gifapp.data.db.Gif
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.FileOutputStream
import java.net.URL
import java.util.concurrent.ArrayBlockingQueue

class FileRepository(private val context: Context) {
    private var favoriteList = ArrayBlockingQueue<Gif>(1024)
    private var db: GifDatabase? = null

    private var _favoriteFlow = MutableStateFlow(favoriteList.size)
    val favoriteFlow: StateFlow<Int> = _favoriteFlow

    private val picturesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    init {
        db = GifDatabase.invoke(context)
        loadFromDatabase()
    }

    fun saveLocally(gif: Gif) {
        val url = gif.gifURL
        val id = gif.id

        val input = URL(url).openStream()
        input.use { inp ->
            val output = FileOutputStream("$picturesDir/$id.gif")
            output.use {
                it.write(inp.readBytes())
            }
        }
    }

    fun loadFromStorage(): List<Gif> {
        val list: MutableList<Uri> = mutableListOf()
        val temp: MutableList<Gif> = mutableListOf()

        picturesDir?.listFiles()?.map {
            val uri = FileProvider.getUriForFile(context, AUTHORITY, it)
            list.add(uri)
        }

        favoriteList.map { gif ->

            val foundUri: Uri? = list.firstOrNull {
                it.path!!.contains(gif.id)
            }

            gif.gifURL = "$foundUri"
            temp.add(gif)
        }

        return temp
    }

    fun addToFavorite(gif: Gif) {
        favoriteList.add(gif)

        CoroutineScope(Dispatchers.IO).launch {
            db?.gifDao()?.insert(gif)
            _favoriteFlow.emit(favoriteList.size)
        }
    }

    fun clearStorage() {
        var dbData: List<Gif> = emptyList()

        CoroutineScope(Dispatchers.IO).launch {
            db?.gifDao()?.let { dbData = it.getAll() }
        }

        picturesDir?.listFiles()?.map { file ->
            val dbGif = dbData.firstOrNull { gif -> file.name.contains(gif.id) }
            if (dbGif == null || !file.name.contains(dbGif.id)) {
                file.deleteRecursively()
            }
        }

        favoriteList.clear()

        CoroutineScope(Dispatchers.IO).launch {
            _favoriteFlow.emit(favoriteList.size)
        }
    }

    fun deleteById(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            db?.let {
                favoriteList.firstOrNull { gif -> gif.id == id }?.let { gif ->
                    it.gifDao().delete(gif)
                }
            }

            favoriteList.apply {
                this.remove(this.firstOrNull { gif -> gif.id == id })
            }

            _favoriteFlow.emit(favoriteList.size)
        }
    }

    fun getFavoriteList(): List<Gif> = favoriteList.toList()


    fun loadFromDatabase(): List<Gif> {
        CoroutineScope(Dispatchers.IO).launch {
            db?.let {
                favoriteList.clear()
                favoriteList.addAll(it.gifDao().getAll())

            }
            _favoriteFlow.emit(favoriteList.size)
        }
        return getFavoriteList()
    }

    companion object {
        private const val AUTHORITY = "com.example.gifapp.fileprovider"
        private var instance: FileRepository? = null

        fun getInstance(context: Context): FileRepository {
            if (instance == null) instance = FileRepository(context)
            return instance as FileRepository
        }
    }
}