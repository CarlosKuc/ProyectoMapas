package com.example.proyecto_riesgos.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.proyecto_riesgos.data.Mapas

@Dao
interface MapasDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMap(mapas: Mapas)

    @Query("DELETE FROM mapas_table")
    fun deleteAll()

    @Query("SELECT * FROM mapas_table")
    suspend fun getall(): List<Mapas>

}