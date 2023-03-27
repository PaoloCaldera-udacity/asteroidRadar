package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.AsteroidRadarApplication
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.ApodApiStatus
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.repository.Asteroid

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

        val adapter = MainListAdapter(MainListAdapter.MainListItemListener {
            mainViewModel.onStartNavigating(it)
        })
        binding.asteroidRecycler.adapter = adapter

        // Observe the image status for displaying the image and update the progress bar
        mainViewModel.imageStatus.observe(viewLifecycleOwner) {
            when (it) {
                ApodApiStatus.LOADING -> binding.apply {
                    statusLoadingWheel.visibility = View.VISIBLE
                    activityMainImageOfTheDay.setImageResource(R.drawable.placeholder_picture_of_day)
                }
                ApodApiStatus.SUCCESS -> {
                    binding.statusLoadingWheel.visibility = View.GONE
                    mainViewModel.image.value?.let {
                        Picasso.get().load(it.url)
                            .placeholder(R.drawable.placeholder_picture_of_day)
                            .into(binding.activityMainImageOfTheDay)
                    }
                }
                else -> { binding.statusLoadingWheel.visibility = View.GONE }
            }
        }

        // Observe the list of asteroids
        mainViewModel.asteroidList.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }

        // Observe the selected asteroid variable for navigation
        mainViewModel.selectedAsteroid.observe(viewLifecycleOwner) {
            it?.let {
                navigateToDetailScreen(it)
                mainViewModel.onStopNavigating()
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
        return true
    }

    private fun navigateToDetailScreen(asteroid: Asteroid) {
        val action = MainFragmentDirections.actionShowDetail(asteroid)
        findNavController().navigate(action)
    }
}
