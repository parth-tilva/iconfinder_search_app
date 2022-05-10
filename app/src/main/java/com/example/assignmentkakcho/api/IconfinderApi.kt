package com.example.assignmentkakcho.api

import com.example.assignmentkakcho.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface IconfinderApi {
    companion object {
        const val BASE_URL = "https://api.iconfinder.com/v4/"
        const val CLIENT_ID = BuildConfig.ICONFINDER_API_KEY
    }

    @Headers("Accept: application/json", "Authorization: Bearer $CLIENT_ID")
    @GET("icons/search")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("count") count: Int,
        @Query("offset") page: Int,
    ): ApiResponse

}