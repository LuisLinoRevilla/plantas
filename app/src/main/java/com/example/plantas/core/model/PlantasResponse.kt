package com.example.plantas.core.model

import com.google.gson.annotations.SerializedName

// Respuesta completa de la API
data class Response(
    @SerializedName("data") val data: List<Planta>  // La API usa "data", no "results"
)

// Para cada planta individual
data class Planta(
    @SerializedName("id") val id: Int,
    @SerializedName("common_name") val commonName: String,
    @SerializedName("scientific_name") val scientificName: List<String>,
    @SerializedName("other_name") val otherName: List<String>,
    @SerializedName("family") val family: String?,
    @SerializedName("genus") val genus: String,
    @SerializedName("default_image") val defaultImage: PlantImage?  // Puede ser null
)

// Para la imagen (es opcional, a veces viene null)
data class PlantImage(
    @SerializedName("license") val license: Int?,
    @SerializedName("license_name") val licenseName: String?,
    @SerializedName("license_url") val licenseUrl: String?,
    @SerializedName("original_url") val originalUrl: String?,
    @SerializedName("regular_url") val regularUrl: String?,
    @SerializedName("medium_url") val mediumUrl: String?,
    @SerializedName("small_url") val smallUrl: String?,
    @SerializedName("thumbnail") val thumbnail: String?
)