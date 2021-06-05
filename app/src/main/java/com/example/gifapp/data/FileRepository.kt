package com.example.gifapp.data

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import com.example.gifapp.model.Gif
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.util.concurrent.ArrayBlockingQueue

private const val TAG = "FileRepository"

class FileRepository(private val context: Context) {
    private var favoriteList = ArrayBlockingQueue<Gif>(1024)
    private var imageDir: File? = null
    private var bitmap: Bitmap? = null

    private var _favoriteFlow = MutableStateFlow(favoriteList.size)
    val favoriteFlow: StateFlow<Int> = _favoriteFlow



    companion object {
        private var instance: FileRepository? = null

        fun getInstance(context: Context): FileRepository {
            if (instance == null) instance = FileRepository(context)
            return instance as FileRepository
        }
    }

    suspend fun saveLocally(gif: Gif) {
        val url = gif.gifURL
        val id = gif.id
        Log.i(TAG, "--> saveLocally: $url")
        Log.i(TAG, "--> saveLocally: $id")

        withContext(Dispatchers.IO) {
            val input = URL(url).openStream()
            input.use { inp ->
                val storage: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                val output = FileOutputStream("$storage/$id.gif")
                output.use {
                    it.write(inp.readBytes())
                }
            }
        }
    }

    fun loadFromStorage(): List<Gif> {
        val list: MutableList<Uri> = mutableListOf()
        val dir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        Log.i(TAG, "--> loadFromStorage: ${dir?.listFiles()?.size}")

        dir?.listFiles()?.forEach {
            Log.i(TAG, "--> loadFromStorage: $it")
            val uri = FileProvider.getUriForFile(context, "gifapp.fileprovider", it)
            list.add(uri)
        }
        val temp: MutableList<Gif> = mutableListOf()
        favoriteList.forEach { gif ->
            Log.i(TAG, "--> loadFromStorage: id[${gif.id}]")

            val foundUri: Uri? = list.firstOrNull {
                it?.path!!.contains(gif.id)
            }

            Log.i(TAG, "--> loadFromStorage: $foundUri")
            gif.gifURL = "$foundUri"
            temp.add(gif)
        }

        return temp
    }

    fun addToFavorite(gif: Gif) {
        Log.i(TAG, "--> addToFavotite: Before.size[${favoriteList.size}]")
        Log.i(TAG, "--> addToFavotite: Before[${gif.id}]")
        favoriteList.add(gif)
        CoroutineScope(Dispatchers.IO).launch {
            _favoriteFlow.emit(favoriteList.size)
        }
        Log.i(TAG, "--> addToFavotite: After[${favoriteList.size}]")
    }

    fun clearStorage() {
        val storage = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        storage?.listFiles()?.forEach { file ->
            try {
                Log.i(TAG, "--> clearStorage: Delete.file[${file.name}")
                file.deleteRecursively()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        /** Created by ID
         * date: 03-Jun-21, 11:42 AM
         * TODO: delete only not favorite files
         */
        favoriteList.apply {
            this.clear()
        }
        CoroutineScope(Dispatchers.IO).launch {
            _favoriteFlow.emit(favoriteList.size)
        }
    }

    fun deleteById(id: String) {
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        Log.i(TAG, "--> deleteFromStorage: before=${dir?.listFiles()?.size}")
//
//        dir?.listFiles()?.filter { gifId ->
//            gifId.name.contains(id)
//        }
//            ?.forEach { it.deleteRecursively() }
        Log.i(TAG, "--> deleteById: BQ.size.before=${favoriteList.size}")



        favoriteList.apply {
            this.remove(this.firstOrNull { gif -> gif.id == id })
        }
        CoroutineScope(Dispatchers.IO).launch {
            _favoriteFlow.emit(favoriteList.size)
        }

        Log.i(TAG, "--> deleteById: BQ.size.after=${favoriteList.size}")
        Log.i(TAG, "--> deleteFromStorage: after=${dir?.listFiles()?.size}")
    }
    fun getFavoriteList(): List<Gif>{
        return favoriteList.toList()
    }
}