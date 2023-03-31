package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.Constants
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
        // Get all the near earth asteroids in the next 7 days
        @GET("neo/rest/v1/feed")
        suspend fun getAsteroids(
            @Query("api_key") apiKey: String,
            @Query("start_date") startDate: String,
            @Query("end_date") endDate: String
        ): String
    }

    val neoWsService: NeoWsService by lazy {
        scalarRetrofit.create(NeoWsService::class.java)
    }
}

enum class NeoWsApiStatus { LOADING, ERROR, SUCCESS }