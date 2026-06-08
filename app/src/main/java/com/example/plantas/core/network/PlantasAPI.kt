package com.example.plantas.core.network

import com.example.plantas.core.model.Planta
import com.example.plantas.core.model.PlantasResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PlantasAPI {

    // Ruta 1: Para la lista infinita de explorar
    @GET("api/v2/species-list")
    suspend fun getSpeciesList(
        @Query("key") apiKey: String,
        @Query("page") page: Int = 1,
        @Query("q") query: String? = null
    ): Response<PlantasResponse>

    // Ruta 2: NUEVA RUTA para los detalles usando el ID
    @GET("api/species/details/{id}")
    suspend fun getSpeciesDetails(
        @Path("id") id: Int,
        @Query("key") apiKey: String
    ): Response<Planta>
}