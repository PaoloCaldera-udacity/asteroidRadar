package com.udacity.asteroidradar

import android.app.Application
import androidx.work.*
import com.udacity.asteroidradar.repository.RefreshCacheWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

class AsteroidRadarApplication : Application() {

    // Set the coroutine scope to launch the worker
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        // Initialize the work manager for refreshing the offline cache every day
        requestPeriodicCacheRefresh()
    }

    private fun requestPeriodicCacheRefresh() {
        applicationScope.launch {
            // Set plug-in charge and network constraints
            val constraints = Constraints.Builder()
                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            // Set the instance of the work request, using the PeriodicWorkRequestBuilder
            val workRequest = PeriodicWorkRequestBuilder<RefreshCacheWorker>(
                1, TimeUnit.DAYS
            ).setConstraints(constraints).build()

            // Use the WorkManager to launch the periodic work
            WorkManager.getInstance().enqueueUniquePeriodicWork(
                RefreshCacheWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
        }
    }
}