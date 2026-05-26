package com.example.plantas.core.network

import com.example.plantas.core.model.Response
import retrofit2.http.GET

interface PlantasAPI {
    @GET(value = "api/v2/species-list?key=sk-6tZF6a15a12da9d4e17626")
    suspend fun getTracks(): Response<PlantaResponse>

}