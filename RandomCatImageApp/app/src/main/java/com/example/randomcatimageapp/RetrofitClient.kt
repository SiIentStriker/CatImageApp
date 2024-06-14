package com.example.randomcatimageapp

// RetrofitClient.kt
// This file contains the RetrofitClient object
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
// The RetrofitClient object is used to create an instance of the Retrofit client
object RetrofitClient {
    private const val BASE_URL = "https://api.thecatapi.com/"
// The apiService property is used to create an instance of the ApiService interface
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}