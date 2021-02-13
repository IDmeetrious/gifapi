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
    private val tempList = ArrayList<Gif>()

    private val _data = MutableLiveData<MutableList<Gif>>()
    val data: LiveData<MutableList<Gif>>
        get() = _data

    init {
        getRandom()
    }

    fun getLatest(page: Int = 0) {
        val latestListCall = repository.apiService.getLatest(page)

        latestListCall.enqueue(object : Callback<GifResponse> {
            override fun onResponse(call: Call<GifResponse>, response: Response<GifResponse>) {
                if (response.isSuccessful && !response.body()?.result.isNullOrEmpty()) {
                    Log.d(TAG, "onResponse: ${response.body()!!.result}")
//                    tempList.addAll(response.body()!!.result!!)
                    _data.value = response.body()!!.result

                } else Log.d(TAG, "--> Latest onError: ${response.errorBody()}")
            }

            override fun onFailure(call: Call<GifResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun getBest(page: Int = 0) {
        val bestListCall = repository.apiService.getLatest(page)

        bestListCall.enqueue(object : Callback<GifResponse> {
            override fun onResponse(call: Call<GifResponse>, response: Response<GifResponse>) {
                if (response.isSuccessful && !response.body()?.result.isNullOrEmpty()) {
                    Log.d(TAG, "onResponse: ${response.body()!!.result}")
//                    tempList.addAll(response.body()!!.result!!)
                    _data.value = response.body()!!.result

                } else Log.d(TAG, "--> Best: onError: ${response.errorBody()}")
            }

            override fun onFailure(call: Call<GifResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
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