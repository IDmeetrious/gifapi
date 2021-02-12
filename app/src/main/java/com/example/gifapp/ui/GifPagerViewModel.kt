package com.example.gifapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gifapp.data.Repository
import com.example.gifapp.model.Gif
import com.example.gifapp.model.GifResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "GifPagerViewModel"

class GifPagerViewModel : ViewModel() {
    private val repository = Repository()
    private val _latestList = MutableLiveData<List<Gif>>()
    val latestList: LiveData<List<Gif>>
        get() = _latestList

    init {
        getLatestGifs()
    }

    fun getLatestGifs(page: Int = 0) {
        val latestListCall = repository.apiService.getLatest(page)

        latestListCall.enqueue(object : Callback<GifResponse> {
            override fun onResponse(call: Call<GifResponse>, response: Response<GifResponse>) {
                if (response.isSuccessful && !response.body()?.result.isNullOrEmpty()) {
                    _latestList.value = response.body()!!.result
                } else Log.d(TAG, "--> onException: ${response.errorBody()}")
            }
            override fun onFailure(call: Call<GifResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

}