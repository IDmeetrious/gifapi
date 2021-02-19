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
import retrofit2.HttpException
import retrofit2.Response

private const val TAG = "GifPageViewModel"

class GifPageViewModel : ViewModel() {
    private val repository = Repository()
    private val tempRandom = ArrayList<Gif>()
    private val tempLatest = ArrayList<Gif>()
    private val tempTop = ArrayList<Gif>()

    private val _data = MutableLiveData<MutableList<Gif>>()
    val data: LiveData<MutableList<Gif>>
        get() = _data

    private val _prevBtnState = MutableLiveData<Boolean>()
    val prevBtnState: LiveData<Boolean>
        get() = _prevBtnState

    init {
        setButtonState(false)
    }

    fun getRandom() {
        val randomGifCall = repository.apiService.getRandom()

        //TODO --> Check Network Connection

            try {
                randomGifCall.enqueue(object : Callback<Gif> {
                    override fun onResponse(call: Call<Gif>, response: Response<Gif>) {
                        if (response.isSuccessful && response.body()?.gifURL?.isEmpty() != true) {
                            tempRandom.add(response.body()!!)
                            _data.value = tempRandom
                            Log.d(TAG, "onRandomResponse: ${response.body()}")
//                } else Log.d(TAG, "--> onRandomError: ${response.errorBody()}")
                        }
                    }

                    override fun onFailure(call: Call<Gif>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
            } catch (e: HttpException) {
                Log.i(TAG, "ERROR getRandom: ${e.message}")
            }


    }

    fun getLatest(page: Int = 0) {
        val latestGifCall = repository.apiService.getLatest(page)
        latestGifCall.enqueue(object : Callback<GifResponse> {
            override fun onResponse(call: Call<GifResponse>, response: Response<GifResponse>) {
                if (response.isSuccessful && !response.body()?.result.isNullOrEmpty()) {
                    tempLatest.addAll(response.body()!!.result!!)
                    _data.value = tempLatest
                    Log.d(TAG, "onLatestResponse: ${response.body()!!.result}")
                } else Log.d(TAG, "--> onLatestError: ${response.errorBody()}")
            }

            override fun onFailure(call: Call<GifResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun getTop(page: Int = 0) {
        val latestGifCall = repository.apiService.getTop(page)
        latestGifCall.enqueue(object : Callback<GifResponse> {
            override fun onResponse(call: Call<GifResponse>, response: Response<GifResponse>) {
                if (response.isSuccessful && !response.body()?.result.isNullOrEmpty()) {
                    tempTop.addAll(response.body()!!.result!!)
                    _data.value = tempTop
                    Log.d(TAG, "onTopResponse: ${response.body()!!.result}")
                } else Log.d(TAG, "--> onTopError: ${response.errorBody()}")
            }

            override fun onFailure(call: Call<GifResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun setButtonState(state: Boolean = true) {
        _prevBtnState.value = state
    }
}