package com.example.proyecto_riesgos.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "mapas_table")
data class Mapas(
    @PrimaryKey (autoGenerate = true)
    val id: Int,
    val Latitudsql: Double,
    val Longitudsql: Double,
    val Radiosql: Double
)