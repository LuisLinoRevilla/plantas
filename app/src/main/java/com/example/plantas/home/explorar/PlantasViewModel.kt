package com.example.plantas.home.explorar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantas.core.ResponseService
import com.example.plantas.core.model.Planta
import com.example.plantas.core.repositories.PlantaRepository
import kotlinx.coroutines.launch

class PlantasViewModel : ViewModel() {

    private val repository = PlantaRepository()

    private val _plantas = MutableLiveData<List<Planta>>()
    val plantas: LiveData<List<Planta>> = _plantas

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var currentPage = 1
    var isCargandoMas = false
    private val listaAcumulada = mutableListOf<Planta>()

    // MOCK DE RESPALDO: Lista fija para no saturar la API
    private val listaDePrueba = listOf(
        Planta(1, "Monstera Deliciosa", listOf("Monstera"), "Araceae", null, "Semanal", null, listOf("Luz indirecta"), "Perenne", "Fácil"),
        Planta(2, "Sansevieria", listOf("Lengua de suegra"), "Asparagaceae", null, "Cada 15 días", null, listOf("Cualquiera"), "Perenne", "Muy fácil")
    )

    fun fetchPlantas(reiniciar: Boolean = false) {
        if (isCargandoMas) return
        if (reiniciar) {
            currentPage = 1
            listaAcumulada.clear()
        }

        viewModelScope.launch {
            isCargandoMas = true
            if (currentPage == 1) _isLoading.value = true

            when (val response = repository.getTracks(currentPage)) {
                is ResponseService.Success -> {
                    val nuevasPlantas = response.data ?: emptyList()
                    if (nuevasPlantas.isNotEmpty()) {
                        listaAcumulada.addAll(nuevasPlantas)
                        _plantas.value = listaAcumulada
                        currentPage++
                    }
                }
                is ResponseService.Error -> {
                    // SI FALLA, CARGAMOS DATOS DE PRUEBA
                    _plantas.value = listaDePrueba
                }

                else -> {}
            }
            _isLoading.value = false
            isCargandoMas = false
        }
    }

    fun buscarPlanta(query: String) {
        if (query.isEmpty()) { fetchPlantas(true); return }

        _isLoading.value = true
        viewModelScope.launch {
            when (val response = repository.searchPlantas(query)) {
                is ResponseService.Success -> {
                    _plantas.value = response.data ?: emptyList()
                }
                is ResponseService.Error -> {
                    // SI FALLA LA BÚSQUEDA, FILTRAMOS EL MOCK LOCAL
                    _plantas.value = listaDePrueba.filter {
                        it.commonName?.contains(query, true) == true
                    }
                }

                else -> {}
            }
            _isLoading.value = false
        }
    }
}