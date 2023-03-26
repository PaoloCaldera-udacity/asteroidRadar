package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.ApodApi
import com.udacity.asteroidradar.api.NeoWsApi
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel : ViewModel() {
    init {
        viewModelScope.launch {
            //Log.i("MainViewModel", NasaApi.neoWsService.getParsedAsteroids()[0].toString())
            try {
                val image = ApodApi.apodService.getImageOfTheDay(Constants.API_KEY)
                val formatter = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
                val date: Date = Calendar.getInstance().time
                Log.i("MainViewModel", date.time.toString())
                Log.i("MainViewModel", image.toString())
            } catch (e: Exception) {
                Log.i("MainViewModel", "${e.message}")
            }

        }
    }
}