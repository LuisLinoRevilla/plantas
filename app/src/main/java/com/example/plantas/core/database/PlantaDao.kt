package com.example.plantas.core.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantaDao {
    @Query("SELECT * FROM plantas_favoritas")
    fun getAllFavoritas(): Flow<List<PlantaFavorita>>

    @Query("SELECT * FROM plantas_favoritas WHERE idApi = :id LIMIT 1")
    suspend fun getFavoritaById(id: Int): PlantaFavorita?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorita(planta: PlantaFavorita)

    @Update
    suspend fun updateFavorita(planta: PlantaFavorita)

    @Delete
    suspend fun deleteFavorita(planta: PlantaFavorita)
}