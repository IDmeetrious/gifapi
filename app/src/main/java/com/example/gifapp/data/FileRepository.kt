package com.example.gifapp.data

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import com.example.gifapp.model.Gif
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.util.concurrent.ArrayBlockingQueue

private const val TAG = "FileRepository"

class FileRepository(private val context: Context) {
    private val favoriteList = ArrayBlockingQueue<Gif>(1024)
    private var imageDir: File? = null
    private var bitmap: Bitmap? = null

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
            Log.i(TAG, "--> saveLocally: ${bitmap?.byteCount?.div(1024)}KB")

            input.use { inp ->
                val storage: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                val output = FileOutputStream("$storage/$id.gif")
                output.use {
                    it.write(inp.readBytes())
                }
            }
        }
    }

    fun loadFromStorage(): List<Gif>{
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
            val foundUri = list.first { uri -> uri.path?.contains(gif.id) ?: false }
            Log.i(TAG, "--> loadFromStorage: $foundUri")
            gif.gifURL = "$foundUri"
            temp.add(gif)
        }

        return temp
    }

    fun addToFavotite(gif: Gif) {
        Log.i(TAG, "--> addToFavotite: Before.size[${favoriteList.size}]")
        Log.i(TAG, "--> addToFavotite: Before[${gif.id}]")
        favoriteList.add(gif)
        Log.i(TAG, "--> addToFavotite: After[${favoriteList.size}]")
    }
}