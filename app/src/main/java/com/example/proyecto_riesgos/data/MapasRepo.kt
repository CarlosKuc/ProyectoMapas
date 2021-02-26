package com.example.proyecto_riesgos.data


import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MapasRepo(private val mapasDao: MapasDao) {

    suspend fun getAll(): List<Mapas> = mapasDao.getall()

    suspend fun addMap(mapas: Mapas){
        mapasDao.addMap(mapas)
    }

    suspend fun deleteAll(){
        mapasDao.deleteAll()
    }
}