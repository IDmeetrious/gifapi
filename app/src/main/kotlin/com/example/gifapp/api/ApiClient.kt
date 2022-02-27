package com.example.gifapp.api

import com.example.gifapp.utils.Constants.API_URL
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    private val retrofitClient = Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    val apiRequests: ApiRequests = retrofitClient.create(ApiRequests::class.java)
}