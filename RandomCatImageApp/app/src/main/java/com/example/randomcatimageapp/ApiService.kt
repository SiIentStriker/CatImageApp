package com.example.randomcatimageapp

// ApiService.kt
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {
    @Headers("x-api-key: live_eZXAuPp26y2Vvwhk7wv38x6RCr0sMmbBMl9eF1KLNNTHzPklNEKnyDIs9MKPA7jG")
    @GET("v1/images/search")
    fun getRandomCatImage(
        @Query("has_breeds") hasBreeds: Int = 1
    ): Call<List<CatImageResponse>>

    @Headers("x-api-key: live_eZXAuPp26y2Vvwhk7wv38x6RCr0sMmbBMl9eF1KLNNTHzPklNEKnyDIs9MKPA7jG")
    @GET("v1/images/search")
    fun getRandomCatImages(
        @Query("limit") limit: Int = 10,
        @Query("has_breeds") hasBreeds: Int = 1
    ): Call<List<CatImageResponse>>
}