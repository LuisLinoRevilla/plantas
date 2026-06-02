package com.example.plantas.core.network

import com.example.plantas.core.model.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlantasAPI {
    @GET("api/v2/species-list")
    suspend fun getSpeciesList(
        @Query("key") apiKey: String,
        @Query("page") page: Int = 1,
        @Query("q") query: String? = null
    ): Response <PlantaResponse> // Retorna Response que contiene la lista de Planta
}