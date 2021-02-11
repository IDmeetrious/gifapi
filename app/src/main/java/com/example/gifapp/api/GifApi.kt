package com.example.gifapp.api

import com.example.gifapp.model.Gif
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GifApi {

    @GET("random")
    fun getRandom(
        @Query("json") json: Boolean
    ) : Call<Gif>

    @GET("latest/{page}")
    fun getLatest(
        @Path("page") page: Int,
        @Query("json") json: Boolean
    ) : Call<List<Gif>>

    @GET("hot/{page}")
    fun getHot(
        @Path("page") page: Int,
        @Query("json") json: Boolean
    ) : Call<List<Gif>>

    @GET("top/{page}")
    fun getTop(
        @Path("page") page: Int,
        @Query("json") json: Boolean
    ) : Call<List<Gif>>
}