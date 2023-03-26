package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.repository.Asteroid
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

/**
 * Connect with the NEOWS web service of the Nasa API to retrieve the near-Earth asteroids
 */

object NeoWsApi {

    private val scalarRetrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(Constants.BASE_URL)
        .build()

    interface NeoWsService {
        @GET("neo/rest/v1/feed")
        suspend fun getAsteroids(@Query("api_key") apiKey: String): String
    }

    // Extension function for retrieving parsed result
    suspend fun NeoWsService.getParsedAsteroids(): List<Asteroid> {
        try {
            val jsonResponse = getAsteroids(Constants.API_KEY)
            return parseAsteroidsJsonResult(JSONObject(jsonResponse))
        } catch (e: Exception) {
            return listOf()
        }
    }

    val neoWsService: NeoWsService by lazy {
        scalarRetrofit.create(NeoWsService::class.java)
    }
}