package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.ApodApi
import com.udacity.asteroidradar.api.NeoWsApi
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    init {
        viewModelScope.launch {
            //Log.i("MainViewModel", NasaApi.neoWsService.getParsedAsteroids()[0].toString())
            try {
                val image = ApodApi.apodService.getImageOfTheDay(Constants.API_KEY)
                Log.i("MainViewModel", image.toString())

                val jsonResult = NeoWsApi.neoWsService.getAsteroidsAsync(Constants.API_KEY)
                Log.i("MainViewModel", jsonResult)
            } catch (e: Exception) {
                Log.i("MainViewModel", "${e.message}")
            }

        }
    }
}