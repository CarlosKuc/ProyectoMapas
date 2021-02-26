package com.example.proyecto_riesgos.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Mapas::class],
    version = 1
)
abstract class MapasDB : RoomDatabase() {
    abstract fun mapasDao(): MapasDao
    companion object {
        @Volatile
        private var INSTANCE : MapasDB?= null
        fun getDatabase(context: Context): MapasDB {
            val tempInstance =
                INSTANCE
            if(tempInstance!=null) {
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MapasDB::class.java,
                    "MapasDB"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}