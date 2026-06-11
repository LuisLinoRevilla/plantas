package com.example.plantas.core.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    const val API_KEY = "sk-2PCl6a26018a66a3a17626"
    private const val BASE_URL = "https://perenual.com/api/"
    private const val MOCK_BASE_URL = "https://mis-plantas.free.beeceptor.com/"

    // Configuración flexible
    private val gson = GsonBuilder().setLenient().create()

    // Cliente Real
    private val retrofitReal = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson)) // <--- Aplicado
        .build()

    val apiReal: PlantasAPI = retrofitReal.create(PlantasAPI::class.java)

    // Cliente Falso
    private val retrofitMock = Retrofit.Builder()
        .baseUrl(MOCK_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson)) // <--- Aplicado
        .build()

    val apiMock: MockPlantaService = retrofitMock.create(MockPlantaService::class.java)
}