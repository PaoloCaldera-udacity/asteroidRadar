package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.ApodApi
import com.udacity.asteroidradar.api.NeoWsApi
import com.udacity.asteroidradar.api.NetworkAsteroid
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.NasaDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject


class AsteroidRepository(private val database: NasaDatabase) {

    val asteroids: LiveData<List<Asteroid>?> = Transformations.map(database.dao.selectByDate()) {
        asDomainModel(it)
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val jsonResult = NeoWsApi.neoWsService.getAsteroidsAsync(Constants.API_KEY)
            val networkResult = parseAsteroidsJsonResult(JSONObject(jsonResult)).toTypedArray()
            database.dao.insertAll(*asDatabaseModel(networkResult))
        }
    }

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


    suspend fun getImageOfTheDay(): PictureOfDay {
        return ApodApi.apodService.getImageOfTheDay(Constants.API_KEY)
    }
}