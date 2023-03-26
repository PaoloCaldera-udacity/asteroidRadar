package com.udacity.asteroidradar

import android.app.Application
import androidx.work.*
import com.udacity.asteroidradar.database.NasaDatabase
import com.udacity.asteroidradar.repository.RefreshCacheWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

class AsteroidRadarApplication : Application() {

    // Initialize the database in the application context
    val database: NasaDatabase by lazy {
        NasaDatabase.getDatabase(this)
    }

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        // Initialize the work manager for refreshing the offline cache every day
        requestPeriodicCacheRefresh()
    }

    private fun requestPeriodicCacheRefresh() {
        applicationScope.launch {
            val constraints = Constraints.Builder()
                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val workRequest = PeriodicWorkRequestBuilder<RefreshCacheWorker>(
                1, TimeUnit.DAYS
            ).setConstraints(constraints).build()

            WorkManager.getInstance().enqueueUniquePeriodicWork(
                RefreshCacheWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
        }
    }
}