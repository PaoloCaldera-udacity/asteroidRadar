package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.NasaDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber


class AsteroidRepository(private val database: NasaDatabase) {

    // Get the next-week data from the offline cache
    val nextWeekAsteroids: LiveData<List<Asteroid>?> =
        Transformations.map(database.dao.selectNextWeek()) { asDomainModel(it) }

    // Get the today data from the offline cache
    val todayAsteroids: LiveData<List<Asteroid>?> =
        Transformations.map(database.dao.selectToday()) { asDomainModel(it) }

    // Get the whole data from the offline cache
    val allAsteroids: LiveData<List<Asteroid>?> =
        Transformations.map(database.dao.selectAll()) { asDomainModel(it) }


    // Refresh the offline cache with the new data coming from the network
    suspend fun refreshAsteroids(startDate: String, endDate: String) {
        withContext(Dispatchers.IO) {
            try {
                val jsonResult = NeoWsApi.neoWsService.getAsteroids(
                    Constants.API_KEY, startDate, endDate
                )
                val networkResult = parseAsteroidsJsonResult(JSONObject(jsonResult)).toTypedArray()
                database.dao.insertAll(*asDatabaseModel(networkResult))
            } catch (e: Exception) {
                Timber.e("Unable to refresh offline cache: ${e.message}")
                throw (e)
            }
        }
    }

    // Delete old data from the offline cache
    suspend fun clearOldData() {
        withContext(Dispatchers.IO) {
            database.dao.deleteOld()
        }
    }

    // Convert the network variable and the database variable
    private fun asDatabaseModel(networkAsteroids: Array<NetworkAsteroid>): Array<DatabaseAsteroid> {
        val databaseAsteroids = ArrayList<DatabaseAsteroid>()
        for (item in networkAsteroids) {
            databaseAsteroids.add(
                DatabaseAsteroid(
                    id = item.id,
                    codename = item.codename,
                    closeApproachDate = item.closeApproachDate,
                    absoluteMagnitude = item.absoluteMagnitude,
                    estimatedDiameter = item.estimatedDiameter,
                    relativeVelocity = item.relativeVelocity,
                    distanceFromEarth = item.distanceFromEarth,
                    isPotentiallyHazardous = item.isPotentiallyHazardous
                )
            )
        }
        return databaseAsteroids.toTypedArray()
    }

    // Convert the database variable into the domain variable
    private fun asDomainModel(databaseAsteroids: List<DatabaseAsteroid>): List<Asteroid> {
        return databaseAsteroids.map {
            Asteroid(
                id = it.id,
                codename = it.codename,
                closeApproachDate = it.closeApproachDate,
                absoluteMagnitude = it.absoluteMagnitude,
                estimatedDiameter = it.estimatedDiameter,
                relativeVelocity = it.relativeVelocity,
                distanceFromEarth = it.distanceFromEarth,
                isPotentiallyHazardous = it.isPotentiallyHazardous
            )
        }
    }


    // Get the image of the day from the network
    suspend fun getImageOfTheDay(): PictureOfDay {
        return ApodApi.apodService.getImageOfTheDay(Constants.API_KEY)
    }
}

enum class SearchMode { NEXT_WEEK, TODAY, ALL }