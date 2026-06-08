package com.example.plantas.core.repositories

import android.util.Log
import com.example.plantas.core.ResponseService
import com.example.plantas.core.model.Planta
import com.example.plantas.core.network.ApiClient
import com.example.plantas.core.network.PlantaService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlantaRepository : PlantaService {

    private val apiReal = ApiClient.apiReal
    private val apiMock = ApiClient.apiMock

    // 1. Fallback para listas generales (Paginación)
    override suspend fun getTracks(page: Int): ResponseService<List<Planta>> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiReal.getSpeciesList(apiKey = ApiClient.API_KEY, page = page)
                if (response.isSuccessful) {
                    val plantas = response.body()?.data ?: emptyList()
                    Log.d("API_REPO", "Éxito API Real (Tracks): ${plantas.size} plantas.")
                    ResponseService.Success(data = plantas)
                } else {
                    Log.w("API_REPO", "Fallo API Real (Tracks): ${response.code()}. Usando Mock.")
                    usarMockApi()
                }
            } catch (e: Exception) {
                Log.e("API_REPO", "Error red (Tracks): ${e.message}. Usando Mock.")
                usarMockApi()
            }
        }

    // 2. Fallback para búsqueda específica
    override suspend fun searchPlantas(query: String): ResponseService<List<Planta>> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiReal.getSpeciesList(apiKey = ApiClient.API_KEY, page = 1, query = query)
                if (response.isSuccessful) {
                    val plantas = response.body()?.data ?: emptyList()
                    ResponseService.Success(data = plantas)
                } else {
                    Log.w("API_REPO", "Fallo búsqueda: ${response.code()}. Filtrando Mock.")
                    usarMockSearch(query)
                }
            } catch (e: Exception) {
                Log.e("API_REPO", "Error red búsqueda: ${e.message}. Filtrando Mock.")
                usarMockSearch(query)
            }
        }

    // 3. Fallback para detalles
    override suspend fun getPlantaDetails(id: Int): ResponseService<Planta> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiReal.getSpeciesDetails(id = id, apiKey = ApiClient.API_KEY)
                if (response.isSuccessful && response.body() != null) {
                    ResponseService.Success(data = response.body()!!)
                } else {
                    ResponseService.Error("Error HTTP: ${response.code()}")
                }
            } catch (e: Exception) {
                ResponseService.Error("Fallo de conexión al obtener detalles: ${e.message}")
            }
        }

    // --- MÉTODOS PRIVADOS DE RESPALDO (MOCK) ---

    private suspend fun usarMockApi(): ResponseService<List<Planta>> {
        return try {
            val mockResponse = apiMock.getMockPlantas()
            if (mockResponse.isSuccessful) {
                val plantas = mockResponse.body()?.data ?: emptyList()
                ResponseService.Success(data = plantas)
            } else {
                ResponseService.Error("Mock falló con código: ${mockResponse.code()}")
            }
        } catch (e: Exception) {
            ResponseService.Error("Error crítico de Mock: ${e.message}")
        }
    }

    private suspend fun usarMockSearch(query: String): ResponseService<List<Planta>> {
        return try {
            val mockResponse = apiMock.getMockPlantas()
            if (mockResponse.isSuccessful) {
                val listaFiltrada = mockResponse.body()?.data?.filter {
                    it.commonName?.contains(query, ignoreCase = true) == true
                } ?: emptyList()
                ResponseService.Success(data = listaFiltrada)
            } else {
                ResponseService.Error("Mock búsqueda falló con código: ${mockResponse.code()}")
            }
        } catch (e: Exception) {
            ResponseService.Error("Error crítico de Mock en búsqueda: ${e.message}")
        }
    }
}