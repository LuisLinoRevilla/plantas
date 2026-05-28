package com.example.plantas.core.repositories

import com.example.plantas.core.ResponseService
import com.example.plantas.core.model.Planta
import com.example.plantas.core.network.ApiClient
import com.example.plantas.core.network.PlantaService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlantaRepository : PlantaService {
    private val api = ApiClient.plantasAPI  // ← Nota: minúscula 'p'

    override suspend fun getTracks(limit: Int): ResponseService<List<Planta>> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.getSpeciesList(  // ← Cambiado a getSpeciesList
                    apiKey = ApiClient.API_KEY,     // ← Usar API_KEY
                    page = 1,
                    query = null
                )
                ResponseService.Success(data = response.data)  // ← response.data
            } catch (e: Exception) {
                ResponseService.Error(
                    "No se pudieron cargar las plantas: ${e.localizedMessage}"
                )
            }
        }
}