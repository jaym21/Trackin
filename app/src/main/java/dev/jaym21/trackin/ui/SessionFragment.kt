package dev.jaym21.trackin.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.PolylineOptions
import dev.jaym21.trackin.R
import dev.jaym21.trackin.databinding.FragmentSessionBinding
import dev.jaym21.trackin.service.Polyline
import dev.jaym21.trackin.service.TrackingService
import dev.jaym21.trackin.util.Constants

class SessionFragment : Fragment() {

    private var _binding: FragmentSessionBinding? = null
    private val binding: FragmentSessionBinding
        get() = _binding!!
    private var map: GoogleMap? = null
    private var isPaused = true
    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSessionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.onCreate(savedInstanceState)

        binding.mapView.getMapAsync {
            map = it
        }

        binding.fabPlayPause.setOnClickListener {
            togglePlayPauseDrawable()
            commandToService(Constants.ACTION_START_OR_RESUME)
        }
    }

    private fun commandToService(action: String) {
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }
    }

    private fun addLatestPolyline() {
        if (pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            val penultimateLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()
            val polylineOptions = PolylineOptions()
                .width(Constants.POLYLINE_WIDTH)
                .color(Constants.POLYLINE_COLOR)
                .add(penultimateLatLng, lastLatLng)

            map?.addPolyline(polylineOptions)
        }
    }

    private fun togglePlayPauseDrawable() {
        isPaused = if (isPaused) {
            binding.fabPlayPause.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_pause))
            false
        } else {
            binding.fabPlayPause.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_play))
            true
        }
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
        _binding = null
    }
}