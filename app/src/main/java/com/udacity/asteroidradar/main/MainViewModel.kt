package com.udacity.asteroidradar.main

import androidx.lifecycle.*
import com.udacity.asteroidradar.AsteroidRadarApplication
import com.udacity.asteroidradar.api.ApodApiStatus
import com.udacity.asteroidradar.api.NeoWsApiStatus
import com.udacity.asteroidradar.database.NasaDatabase
import com.udacity.asteroidradar.repository.Asteroid
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.repository.PictureOfDay
import kotlinx.coroutines.launch

class MainViewModel(application: AsteroidRadarApplication) : ViewModel() {

    // Initialize the repository from which to retrieve cached data
    private val repository = AsteroidRepository(NasaDatabase.getDatabase(application))


    // UI variables
    val asteroidList: LiveData<List<Asteroid>?> = repository.asteroids

    private val _asteroidListStatus = MutableLiveData<NeoWsApiStatus?>()
    val asteroidListStatus: LiveData<NeoWsApiStatus?>
        get() = _asteroidListStatus

    private val _image = MutableLiveData<PictureOfDay?>()
    val image: LiveData<PictureOfDay?>
        get() = _image

    private val _imageStatus = MutableLiveData<ApodApiStatus?>()
    val imageStatus: LiveData<ApodApiStatus?>
        get() = _imageStatus


    // Navigation variables
    private val _selectedAsteroid = MutableLiveData<Asteroid?>(null)
    val selectedAsteroid: LiveData<Asteroid?>
        get() = _selectedAsteroid

    init {
        viewModelScope.launch {
            displayImage()
            displayAsteroidList()
        }
    }

    private suspend fun displayImage() {
        _imageStatus.value = ApodApiStatus.LOADING
        try {
            val image = repository.getImageOfTheDay()
            _image.value = if (image.mediaType != "image") null else image
            _imageStatus.value = ApodApiStatus.SUCCESS
        } catch (e: Exception) {
            _imageStatus.value = ApodApiStatus.ERROR
        }
    }

    private suspend fun displayAsteroidList() {
        _asteroidListStatus.value = NeoWsApiStatus.LOADING
        try {
            repository.refreshAsteroids()
            _asteroidListStatus.value = NeoWsApiStatus.SUCCESS
        } catch (e: Exception) {
            _asteroidListStatus.value = NeoWsApiStatus.ERROR
        }
    }

    fun onStartNavigating(item: Asteroid) {
        _selectedAsteroid.value = item
    }

    fun onStopNavigating() {
        _selectedAsteroid.value = null
    }

    // ViewModelFactory class
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