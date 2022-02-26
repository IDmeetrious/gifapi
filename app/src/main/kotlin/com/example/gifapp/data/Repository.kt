package com.example.gifapp.data

import com.example.gifapp.api.GifApi
import com.example.gifapp.other.Constants.API_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Repository {

    private val retrofitClient = Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: GifApi = retrofitClient.create(GifApi::class.java)
}