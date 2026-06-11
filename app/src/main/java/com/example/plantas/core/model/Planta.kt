package com.example.plantas.core.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Planta(
    val id: Int,
    @SerializedName("common_name") val commonName: String?,
    @SerializedName("scientific_name") val scientificName: List<String>?,
    val family: String?,
    @SerializedName("default_image") val defaultImage: DefaultImage?, // Esta referencia debe coincidir con el nombre de la clase de abajo

    @SerializedName("watering") val watering: String?,
    @SerializedName("watering_general") val wateringGeneral: String?,
    @SerializedName("sunlight") val sunlight: List<String>?,
    @SerializedName("cycle") val cycle: String?,
    @SerializedName("care_level") val careLevel: String?
) : Parcelable

@Parcelize
data class DefaultImage(
    @SerializedName("original_url") val originalUrl: String?,
    @SerializedName("regular_url") val regularUrl: String?,
    @SerializedName("medium_url") val mediumUrl: String?,
    @SerializedName("small_url") val smallUrl: String?,
    @SerializedName("thumbnail") val thumbnail: String?
) : Parcelable