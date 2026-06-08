package com.example.plantas.core.network

import com.example.plantas.core.ResponseService
import com.example.plantas.core.model.Planta

interface PlantaService {
    suspend fun getTracks(page: Int): ResponseService<List<Planta>>

    suspend fun getPlantaDetails(id: Int): ResponseService<Planta>

    suspend fun searchPlantas(query: String): ResponseService<List<Planta>>
}