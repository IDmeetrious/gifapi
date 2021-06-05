package com.example.gifapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gifapp.App
import com.example.gifapp.api.ApiClient
import com.example.gifapp.data.FileRepository
import com.example.gifapp.model.Gif
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

private const val TAG = "GifPageViewModel"

class GifPageViewModel : ViewModel() {

    private val repository = FileRepository.getInstance(App.getContext())

    private val apiClient = ApiClient()
    private val tempRandom = ArrayList<Gif>()

    private val _data = MutableLiveData<MutableList<Gif>>()
    val data: LiveData<MutableList<Gif>>
        get() = _data

    private val _likeBtnState = MutableLiveData<Boolean>()
    val likeBtnState: LiveData<Boolean>
        get() = _likeBtnState

    private var _gifLive = MutableLiveData<Gif>()
    val gifLive: LiveData<Gif>
    get() = _gifLive

    init {
        _likeBtnState.value = false
    }

    fun getRandom() {
        val randomGifCall = apiClient.apiRequests.getRandom()

        try {
            randomGifCall.enqueue(object : Callback<Gif> {
                override fun onResponse(call: Call<Gif>, response: Response<Gif>) {
                    if (response.isSuccessful && response.body()?.gifURL?.isEmpty() != true) {
                        tempRandom.add(response.body()!!)

                        viewModelScope.launch {
                            response.body()?.let {
                                Log.i(TAG, "--> onResponse: ${it.description}\n")
                                _gifLive.postValue(it)

                                repository.saveLocally(it)
                            }
                        }
                        _data.value = tempRandom
                        Log.i(TAG, "onRandomResponse: ${response.body()}")
                    }
                }

                override fun onFailure(call: Call<Gif>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        } catch (e: HttpException) {
            Log.e(TAG, "ERROR getRandom: ${e.message}")
        }
    }

    fun updateLikeBtnState() {
        likeBtnState.value.let {
            _likeBtnState.value = !it!!
        }
    }

    fun setLikeBtnState(state: Boolean) {
        _likeBtnState.postValue(state)
    }

    fun addFavorite(gif: Gif){
        repository.addToFavorite(gif)
    }

    fun getFavorite(gif: Gif): Boolean{
        return repository.getFavoriteList().contains(gif)
    }

    fun clearRepository(){
        Log.i(TAG, "clearRepository: ")
        CoroutineScope(Dispatchers.IO).launch {
            repository.clearStorage()
        }
    }

    fun deleteFavorite(gif: Gif) {
        repository.deleteById(gif.id)
    }
}