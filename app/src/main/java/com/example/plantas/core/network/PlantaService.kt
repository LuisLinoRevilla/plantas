package com.example.plantas.core.network

import com.example.plantas.core.ResponseService
import com.example.plantas.core.model.Planta

interface PlantaService {
    suspend fun getTracks(limit: Int = 20): ResponseService<List<Planta>>
}