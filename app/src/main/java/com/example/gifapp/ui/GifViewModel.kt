package com.example.gifapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gifapp.data.Repository
import com.example.gifapp.model.Gif
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "GifViewModel"

class GifViewModel : ViewModel() {

    private val repository = Repository()

    private val _randomGif = MutableLiveData<Gif>()
    val randomGif: LiveData<Gif>
        get() = _randomGif

    fun getRandom() {
        val randomGif = repository.apiService.getRandom(true)
        randomGif.enqueue(object : Callback<Gif> {
            override fun onResponse(call: retrofit2.Call<Gif>, response: Response<Gif>) {
                if (response.isSuccessful){
                    Log.d(TAG, "--> onResponse: Successful")
                    Log.d(TAG, "--> id: ${response.body()?.id}")
                    Log.d(TAG, "--> description: ${response.body()?.description}")
                    Log.d(TAG, "--> gifURL: ${response.body()?.gifURL}")

                    _randomGif.value = response.body()
                }
            }

            override fun onFailure(call: retrofit2.Call<Gif>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}