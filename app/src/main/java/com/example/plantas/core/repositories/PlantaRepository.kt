package com.example.plantas.core.repositories

import com.example.plantas.core.ResponseService
import com.example.plantas.core.network.ApiClient
import com.example.plantas.core.network.PlantaService

class PlantaRepository: PlantaService{
    private val api = ApiClient.PlantasAPI

    override suspend fun getTracks(limit: Int): ResponseService<List<Planta>>=
    withContext(Dispatchers.IO) {
        try {
            val response = api.getTracks(
                clientId = ApiClient.CLIENT_ID,
                limit = limit
            )
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    ResponseService.Success(data = body.results)
                } else {
                    ResponseService.Error("Respuesta vacía del servidor")
                }
            } else {
                ResponseService.Error("Error ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            ResponseService.Error(
                "No se pudieron cargar las plantas: ${e.localizedMessage}"
            )
        }
    }
}
