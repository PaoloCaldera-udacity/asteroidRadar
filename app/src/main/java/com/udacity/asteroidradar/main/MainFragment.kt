package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.AsteroidRadarApplication
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.ApodApiStatus
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.repository.Asteroid
import com.udacity.asteroidradar.repository.SearchMode

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((activity?.application) as AsteroidRadarApplication)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.apply {
            viewModel = mainViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        // Observe the image status for displaying the image and update the progress bar
        mainViewModel.imageStatus.observe(viewLifecycleOwner) {
            when (it) {
                ApodApiStatus.LOADING -> binding.apply {
                    statusLoadingWheel.visibility = View.VISIBLE
                    activityMainImageOfTheDay.apply {
                        setImageResource(R.drawable.placeholder_picture_of_day)
                        contentDescription =
                            resources.getString(R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet)
                    }
                }
                ApodApiStatus.SUCCESS -> {
                    mainViewModel.image.value?.let {
                        Picasso.get().load(it.url)
                            .placeholder(R.drawable.placeholder_picture_of_day)
                            .into(binding.activityMainImageOfTheDay)
                    }
                    binding.apply {
                        activityMainImageOfTheDay.contentDescription = resources.getString(
                            R.string.nasa_picture_of_day_content_description_format,
                            mainViewModel.image.value?.title
                        )
                        statusLoadingWheel.visibility = View.GONE
                    }
                }
                else -> binding.apply {
                    statusLoadingWheel.visibility = View.GONE
                    activityMainImageOfTheDayLayout.visibility = View.GONE
                }
            }
        }

        // Observe the snackbar event trigger for triggering the snackbar
        mainViewModel.snackbarTrigger.observe(viewLifecycleOwner) {
            if (it == true) {
                displaySnackbar()
                mainViewModel.onSnackbarDismissed()
            }
        }


        // Observe the selected asteroid variable for navigation
        mainViewModel.selectedAsteroid.observe(viewLifecycleOwner) {
            it?.let {
                navigateToDetailScreen(it)
                mainViewModel.onStopNavigating()
            }
        }

        // Set the recycler view adapter
        val adapter = MainListAdapter(MainListAdapter.MainListItemListener {
            mainViewModel.onStartNavigating(it)
        })
        binding.asteroidRecycler.adapter = adapter

        // Observe the list of asteroids
        mainViewModel.asteroidList.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        mainViewModel.changeSearchMode(
            when (item.itemId) {
                R.id.show_week -> SearchMode.NEXT_WEEK
                R.id.show_today -> SearchMode.TODAY
                R.id.show_all -> SearchMode.ALL
                else -> return false
            }
        )
        return true
    }

    private fun navigateToDetailScreen(asteroid: Asteroid) {
        val action = MainFragmentDirections.actionShowDetail(asteroid)
        findNavController().navigate(action)
    }

     // Display a snackbar in case of no internet connection
    private fun displaySnackbar() {
        Snackbar.make(
            binding.root,
            resources.getString(R.string.snackbar_text),
            Snackbar.LENGTH_LONG
        ).show()
    }
}
