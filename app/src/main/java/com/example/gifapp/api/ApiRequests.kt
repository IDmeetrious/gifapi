package com.example.gifapp.api

import com.example.gifapp.model.Gif
import com.example.gifapp.model.GifResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiRequests {

    @GET("random")
    fun getRandom(
        @Query("json") json: Boolean = true
    ): Single<Gif>

    @GET("top/{page}?json=true")
    fun getTop(
        @Path("page") page: Int,
        @Query("json") json: Boolean = true
    ): Single<GifResponse>
}