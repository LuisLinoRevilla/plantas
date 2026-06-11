package com.example.plantas.core.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "plantas_favoritas")
data class PlantaFavorita(
    @PrimaryKey(autoGenerate = true) val idLocal: Int = 0,
    val idApi: Int,
    val nombreOriginal: String,
    var apodo: String,
    var imagenLocalUri: String,
    val riego: String? = "",
    val sol: String? = "",
    val tipo: String? = "",
    val cuidados: String? = "",
    val cuidadosEspecificos: String? = ""
) : Parcelable