package com.example.plantas.home.detallesPlanta

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantas.core.ResponseService
import com.example.plantas.core.model.Planta
import com.example.plantas.core.repositories.PlantaRepository
import kotlinx.coroutines.launch

class DetallesViewModel : ViewModel() {

    private val repository = PlantaRepository()

    private val _plantaDetalle = MutableLiveData<Planta>()
    val plantaDetalle: LiveData<Planta> = _plantaDetalle

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchDetalles(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            Log.d("API_DETALLES", "Pidiendo ficha clínica para la planta ID: $id...")

            when (val response = repository.getPlantaDetails(id)) {
                is ResponseService.Success -> {
                    Log.d("API_DETALLES", "¡Éxito! Cuidados recibidos: ${response.data?.careLevel}")
                    _plantaDetalle.value = response.data
                }
                is ResponseService.Error -> {
                    // ¡Aquí atrapamos si Perenual nos bloquea o hay error de red!
                    Log.e("API_DETALLES", "Fallo al bajar detalles: ${response.toString()}")
                }
                else -> {}
            }

            _isLoading.value = false
        }
    }
}
