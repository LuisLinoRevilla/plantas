package com.example.plantas.core.model
import com.google.gson.annotations.SerializedName

data class Response (
    @SerializedName("results") val results: List<Planta>
)

data class Planta()