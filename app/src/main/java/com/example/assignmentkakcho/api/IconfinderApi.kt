package com.example.assignmentkakcho.api

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface IconfinderApi {
    companion object {
        const val BASE_URL = "https://api.iconfinder.com/v4/"
        const val CLIENT_ID = "TjvjWYVYynw2O0Ek1OQtfr4Hbo0znyJNxVBJ0MKCZhwqR5LlYcuchNcLh4DHbF06"
    }

    @Headers("Accept: application/json", "Authorization: Bearer $CLIENT_ID")
    @GET("icons/search")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("count") count: Int,
        @Query("offset") page: Int,
    ): SearchResponse


    @Headers("Accept: application/json", "Authorization: Bearer $CLIENT_ID")
    @GET("categories")
    suspend fun getCategories(
        @Query("count") count: Int,
        @Query("after") afterIdentifier: String?,
    ): CategoriesResponse

    @Headers("Accept: application/json", "Authorization: Bearer $CLIENT_ID")
    @GET("categories")
    suspend fun getIconSets(
        @Query("count") count: Int,
        @Query("after") afterIdentifier: String?,
    ): CategoriesResponse


}