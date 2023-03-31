package com.udacity.asteroidradar.main

import androidx.lifecycle.*
import com.udacity.asteroidradar.AsteroidRadarApplication
import com.udacity.asteroidradar.api.ApodApiStatus
import com.udacity.asteroidradar.api.NeoWsApiStatus
import com.udacity.asteroidradar.database.NasaDatabase
import com.udacity.asteroidradar.repository.Asteroid
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.repository.PictureOfDay
import com.udacity.asteroidradar.repository.SearchMode
import kotlinx.coroutines.launch

class MainViewModel(application: AsteroidRadarApplication) : ViewModel() {

    // Initialize the repository from which to retrieve cached data
    private val repository = AsteroidRepository(NasaDatabase.getDatabase(application))


    // Search variable
    private val searchMode = MutableLiveData(SearchMode.TODAY)

    // UI variables
    val asteroidList: LiveData<List<Asteroid>?> = Transformations.switchMap(searchMode) {
        when (it!!) {
            SearchMode.NEXT_WEEK -> repository.nextWeekAsteroids
            SearchMode.TODAY -> repository.todayAsteroids
            SearchMode.ALL -> repository.allAsteroids
        }
    }

    private val _image = MutableLiveData<PictureOfDay?>()
    val image: LiveData<PictureOfDay?>
        get() = _image


    // Status variables
    private val _asteroidListStatus = MutableLiveData<NeoWsApiStatus?>()
    val asteroidListStatus: LiveData<NeoWsApiStatus?>
        get() = _asteroidListStatus

    private val _imageStatus = MutableLiveData<ApodApiStatus?>()
    val imageStatus: LiveData<ApodApiStatus?>
        get() = _imageStatus


    // Navigation variables
    private val _selectedAsteroid = MutableLiveData<Asteroid?>(null)
    val selectedAsteroid: LiveData<Asteroid?>
        get() = _selectedAsteroid


    // Trigger variables
    private val _snackbarTrigger = MutableLiveData(false)
    val snackbarTrigger: LiveData<Boolean>
        get() = _snackbarTrigger


    init {
        viewModelScope.launch {
            displayImage()
            displayAsteroidList()
        }
    }

    // Display the image of the day, based on the APOD api status
    private suspend fun displayImage() {
        _imageStatus.value = ApodApiStatus.LOADING
        try {
            val image = repository.getImageOfTheDay()
            _image.value = if (image.mediaType != "image") null else image
            _imageStatus.value = ApodApiStatus.SUCCESS
        } catch (e: Exception) {
            _imageStatus.value = ApodApiStatus.ERROR
            _snackbarTrigger.value = true
        }
    }

    // Display the asteroid list, based on the NeoWs api status
    private suspend fun displayAsteroidList() {
        _asteroidListStatus.value = NeoWsApiStatus.LOADING
        try {
            repository.refreshAsteroids()
            _asteroidListStatus.value = NeoWsApiStatus.SUCCESS
        } catch (e: Exception) {
            _asteroidListStatus.value = NeoWsApiStatus.ERROR
            _snackbarTrigger.value = true
        }
    }


    // Change the search mode according to the option selected in the UI
    fun changeSearchMode(mode: SearchMode) {
        searchMode.value = mode
    }

    // Set the selected asteroid as the trigger variable
    fun onStartNavigating(item: Asteroid) {
        _selectedAsteroid.value = item
    }

    // Set the selected asteroid as nillable when navigation is done
    fun onStopNavigating() {
        _selectedAsteroid.value = null
    }

    // Set the snackbar trigger variable to false when snackbar is dismissed
    fun onSnackbarDismissed() {
        _snackbarTrigger.value = false
    }


    // ViewModelFactory class
    class MainViewModelFactory(private val application: AsteroidRadarApplication) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(application) as T
            }
            throw IllegalArgumentException("ViewModel class not found: unable to create ViewModel")
        }
    }
}