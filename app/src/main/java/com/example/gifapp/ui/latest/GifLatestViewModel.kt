package com.example.gifapp.ui.latest

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

private const val TAG = "GifLatestViewModel"

class GifLatestViewModel : ViewModel() {
    private val repository = Repository()
    private val tempList = ArrayList<Gif>()

    private val _data = MutableLiveData<MutableList<Gif>>()
    val data: LiveData<MutableList<Gif>>
        get() = _data

    init {
        getLatest()
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

}