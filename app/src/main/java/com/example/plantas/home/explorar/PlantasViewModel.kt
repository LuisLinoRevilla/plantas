package com.example.plantas.home.explorar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantas.core.ResponseService
import com.example.plantas.core.network.PlantaService
import com.example.plantas.core.repositories.PlantaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlantasViewModel (
    private val service: PlantaService = PlantaRepository()
): ViewModel(){
    private val _plantaState = MutableStateFlow<ResponseService<List<Planta>>?>(null)
    val PlantaState: StateFlow<ResponseService<List<Planta>>?> = _plantaState.asStateFlow()

    fun loadPlantas(limit: Int = 20)
    {viewModelScope.launch {
        _plantaState.value = ResponseService.Loading
        _plantaState.value = service.getTracks(limit)
    }}

}