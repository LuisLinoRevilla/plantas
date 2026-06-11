package com.example.plantas.core.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

// Respuesta completa de la API
data class PlantasResponse(
    @SerializedName("data") val data: List<Planta>
)