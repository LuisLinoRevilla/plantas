package com.example.plantas.core.network

import com.example.plantas.core.model.PlantasResponse
import com.example.plantas.core.model.Planta
import retrofit2.Response
import retrofit2.http.GET

interface MockPlantaService {
    @GET("mock-species-list")
    suspend fun getMockPlantas(): Response<PlantasResponse>

    // Opcional: si quieres buscar en el mock
    @GET("mock-search")
    suspend fun searchPlantasMock(@retrofit2.http.Query("q") query: String): Response<PlantasResponse>
}