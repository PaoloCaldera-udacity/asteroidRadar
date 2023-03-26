package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DatabaseAsteroid::class], version = 1, exportSchema = false)
abstract class NasaDatabase: RoomDatabase() {

    abstract val dao: NasaDatabaseDao

    companion object {
        private var INSTANCE: NasaDatabase? = null

        fun getDatabase(context: Context): NasaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NasaDatabase::class.java,
                    "nasa_database"
                ).build()

                INSTANCE = instance

                instance
            }
        }
    }

}