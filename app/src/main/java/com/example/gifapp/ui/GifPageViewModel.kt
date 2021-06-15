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
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "GifPageViewModel"

class GifPageViewModel : ViewModel() {

    private val repository = FileRepository.getInstance(App.getInstance().applicationContext)
    private var disposable: Disposable? = null

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
        val randomGif = apiClient.apiRequests.getRandom()

        disposable = randomGif
            .subscribeOn(Schedulers.io())
            .subscribe({ response ->
                response?.let {
                    Log.i(
                        TAG, "--> getRandom: ${it.id}" +
                                it.description
                    )
                    tempRandom.add(it)
                    _gifLive.postValue(it)
                    viewModelScope.launch {
                        repository.saveLocally(it)
                    }
                    _data.postValue(tempRandom)
                }
            }, { error ->
                error.cause
            })
    }

    fun updateLikeBtnState() {
        likeBtnState.value.let {
            _likeBtnState.value = !it!!
        }
    }

    fun setLikeBtnState(state: Boolean) {
        _likeBtnState.postValue(state)
    }

    fun addFavorite(gif: Gif) {
        repository.addToFavorite(gif)
    }

    fun getFavorite(gif: Gif): Boolean {
        return repository.getFavoriteList().contains(gif)
    }

    fun clearRepository() {
        Log.i(TAG, "clearRepository: ")
        CoroutineScope(Dispatchers.IO).launch {
            repository.clearStorage()
        }
    }

    fun deleteFavorite(gif: Gif) {
        repository.deleteById(gif.id)
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }
}