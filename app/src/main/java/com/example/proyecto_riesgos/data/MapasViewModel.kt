package com.example.proyecto_riesgos.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MapasViewModel(application: Application): AndroidViewModel(application) {


    private val repo: MapasRepo

    init{
        val mapasDao = MapasDB.getDatabase(application).mapasDao()
        repo = MapasRepo(mapasDao)
    }

    fun getAll(): LiveData<List<Mapas>>{
        val res: MutableLiveData<List<Mapas>> = MutableLiveData()
        viewModelScope.launch {
            res.postValue(repo.getAll())
        }
        return res
    }

    fun addMap(mapas: Mapas){
        viewModelScope.launch(Dispatchers.IO) {
            repo.addMap(mapas)
        }

    }

    fun deleteAll(){
        viewModelScope.launch(Dispatchers.IO){
            repo.deleteAll()
        }
    }
}