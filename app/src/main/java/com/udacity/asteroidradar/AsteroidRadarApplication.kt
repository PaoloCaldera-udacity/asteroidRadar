package com.udacity.asteroidradar

import android.app.Application
import com.udacity.asteroidradar.database.NasaDatabase
import timber.log.Timber

class AsteroidRadarApplication: Application() {

    // Initialize the database in the application context
    val database: NasaDatabase by lazy {
        NasaDatabase.getDatabase(this)
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

}