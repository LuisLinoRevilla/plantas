package com.example.plantas.core.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://perenual.com/api/v2/species-list?key=sk-6tZF6a15a12da9d4e17626"
    const val CLIENT_ID = "sk-6tZF6a15a12da9d4e17626"

    private val loggin = HttpLoggingInterceptor().apply{
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor (loggin)
        .build()

    val PlantasAPI by lazy {
        Retrofit.Builder()

            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlantasAPI::class.java)
    }
}