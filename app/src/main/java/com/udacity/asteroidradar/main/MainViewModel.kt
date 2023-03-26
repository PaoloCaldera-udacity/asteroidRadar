package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.AsteroidRadarApplication
import com.udacity.asteroidradar.repository.Asteroid
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class MainViewModel(application: AsteroidRadarApplication) : ViewModel() {

    // Initialize the repository from which to retrieve cached data
    private val repository = AsteroidRepository(application.database)

    // UI variables
    val asteroids: LiveData<List<Asteroid>> = repository.asteroids

    init {
        viewModelScope.launch {
            repository.refreshAsteroids()
        }
    }

    class MainViewModelFactory(private val application: AsteroidRadarApplication) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(application) as T
            }
            throw IllegalArgumentException("ViewModel class not found: unable to create ViewModel")
        }

    }
}