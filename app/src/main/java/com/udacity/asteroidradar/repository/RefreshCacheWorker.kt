package com.udacity.asteroidradar.repository

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.AsteroidRadarApplication

class RefreshCacheWorker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        // Get an instance of the repository, passing the app database reference
        val repository = AsteroidRepository(
            (applicationContext as AsteroidRadarApplication).database
        )

        return try {
            // Refresh the offline cache (the database)
            repository.refreshAsteroids()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}