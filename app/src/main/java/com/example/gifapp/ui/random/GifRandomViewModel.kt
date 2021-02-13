package com.example.gifapp.ui.random

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gifapp.data.Repository
import com.example.gifapp.model.Gif
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "GifRandomViewModel"

class GifRandomViewModel : ViewModel() {
    private val repository = Repository()
    private val tempList = ArrayList<Gif>()

    private val _data = MutableLiveData<MutableList<Gif>>()
    val data: LiveData<MutableList<Gif>>
        get() = _data

    init {
        getRandom()
    }

    fun getRandom(json: Boolean = true) {
        val randomGifCall = repository.apiService.getRandom(json)
        randomGifCall.enqueue(object : Callback<Gif> {
            override fun onResponse(call: Call<Gif>, response: Response<Gif>) {
                if (response.isSuccessful && response.body()?.gifURL?.isEmpty() != true) {
                    tempList.add(response.body()!!)
                    _data.value = tempList
                    Log.d(TAG, "onResponse: ${response.body()}")
                } else Log.d(TAG, "--> onError: ${response.errorBody()}")
            }

            override fun onFailure(call: Call<Gif>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

}