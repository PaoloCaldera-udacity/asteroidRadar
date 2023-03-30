package com.udacity.asteroidradar.detail


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentDetailBinding
import com.udacity.asteroidradar.repository.Asteroid

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var selectedAsteroid: Asteroid

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        selectedAsteroid = DetailFragmentArgs.fromBundle(requireArguments()).selectedAsteroid

        binding = FragmentDetailBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            asteroid = selectedAsteroid

            helpButton.setOnClickListener {
                displayAstronomicalUnitExplanationDialog()
            }

            activityMainImageOfTheDay.contentDescription = resources.getString(
                if (selectedAsteroid.isPotentiallyHazardous) R.string.potentially_hazardous_asteroid_image
                else R.string.not_hazardous_asteroid_image
            )
        }

        return binding.root
    }

    private fun displayAstronomicalUnitExplanationDialog() {
        val builder = AlertDialog.Builder(requireActivity())
            .setMessage(getString(R.string.astronomica_unit_explanation))
            .setPositiveButton(android.R.string.ok, null)
        builder.create().show()
    }
}
