package com.example.gifapp.api

import com.example.gifapp.data.db.Gif
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiRequests {

    @GET("random")
    fun getRandom(
        @Query("json") json: Boolean = true
    ): Single<Gif>
}