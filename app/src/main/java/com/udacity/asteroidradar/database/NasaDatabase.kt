package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DatabaseAsteroid::class], version = 1, exportSchema = false)
abstract class NasaDatabase : RoomDatabase() {

    // Variable for retrieving the dao instance
    abstract val dao: NasaDatabaseDao

    companion object {
        // Volatile: all the running threads access to the same instance, and so to the same value
        @Volatile
        private var INSTANCE: NasaDatabase? = null

        // Create a database instance or get the existing one
        fun getDatabase(context: Context): NasaDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NasaDatabase::class.java,
                        "nasa_database"
                    ).fallbackToDestructiveMigration().build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}