package dev.jaym21.trackin.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.jaym21.trackin.R
import dev.jaym21.trackin.databinding.FragmentSessionBinding
import dev.jaym21.trackin.model.Session
import dev.jaym21.trackin.service.Polyline
import dev.jaym21.trackin.service.TrackingService
import dev.jaym21.trackin.util.Constants
import dev.jaym21.trackin.util.Utilities
import dev.jaym21.trackin.viewmodel.MainViewModel
import java.util.*
import javax.inject.Inject
import kotlin.math.round

@AndroidEntryPoint
class SessionFragment : Fragment() {

    private var _binding: FragmentSessionBinding? = null
    private val binding: FragmentSessionBinding
        get() = _binding!!
    private val mainViewModel: MainViewModel by viewModels()
    private var map: GoogleMap? = null
    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()
    private var currentTimeInMillis = 0L
    @set:Inject
    var userWeight = 70f

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
            addAllPolylinesOnMap()
        }

        binding.fabPlayPause.setOnClickListener {
            if (isTracking) {
                commandToService(Constants.ACTION_PAUSE)
            } else {
                binding.ivCancelSession.visibility = View.VISIBLE
                commandToService(Constants.ACTION_START_OR_RESUME)
            }
        }

        binding.ivCancelSession.setOnClickListener {
            if (currentTimeInMillis > 0L) {
                showCancelSessionDialog()
            }
        }

        binding.ivStopSession.setOnClickListener {
            zoomOutToWholeTrack()
            endSessionAndSaveToDatabase()
        }

        TrackingService.isTracking.observe(viewLifecycleOwner) {
            //when tracking state is changed
            updateTracking(it)
        }

        TrackingService.pathPoints.observe(viewLifecycleOwner) {
            //when new path point is added
            pathPoints = it
            addLatestPolyline()
            moveCameraToLocation()
        }

        TrackingService.sessionTimeInMillis.observe(viewLifecycleOwner) {
            currentTimeInMillis = it
            val timerFormat = Utilities.timeToTimerFormat(it, true)
            binding.tvTimer.text = timerFormat
        }
    }

    //update button state according to current isTracking state
    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        if (isTracking) {
            binding.fabPlayPause.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_pause))
            binding.ivStopSession.visibility = View.GONE
        } else if (!isTracking && currentTimeInMillis > 0L) {
            binding.fabPlayPause.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_play))
            binding.ivCancelSession.visibility = View.VISIBLE
            binding.ivStopSession.visibility = View.VISIBLE
        }
    }

    //function to send action to service
    private fun commandToService(action: String) {
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }
    }

    private fun showCancelSessionDialog() {
        val builder = AlertDialog.Builder(requireContext()).create()

        val view = layoutInflater.inflate(R.layout.cancel_session_layout, null)
        val noButton: TextView = view.findViewById(R.id.tvNoCancelDialog)
        val yesButton: TextView = view.findViewById(R.id.tvYesCancelDialog)

        builder.setView(view)
        builder.setCanceledOnTouchOutside(false)

        yesButton.setOnClickListener {
            Snackbar.make(binding.root, "Session has been cancelled!", Snackbar.LENGTH_SHORT).show()
            builder.dismiss()
            endSession()
        }
        noButton.setOnClickListener {
            builder.dismiss()
        }
        builder.show()
    }

    //function to end the session and not save the session data to database
    private fun endSession() {
        commandToService(Constants.ACTION_STOP)
        findNavController().popBackStack()
    }

    //function to add latest polyline on map and connect it with the previous polyline
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

    //to add all polylines on map incase the fragment is recreated due to rotation of device
    private fun addAllPolylinesOnMap() {
        for (polyline in pathPoints) {
            val polylineOptions = PolylineOptions()
                .width(Constants.POLYLINE_WIDTH)
                .color(Constants.POLYLINE_COLOR)
                .addAll(polyline)

            map?.addPolyline(polylineOptions)
        }
    }

    //move camera to the latest location of user in case of addition of a polyline
    private fun moveCameraToLocation() {
        if (pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(
                pathPoints.last().last(),
                Constants.DEFAULT_MAP_ZOOM
            ))
        }
    }

    //zoom out to capture the whole track of session for the session track bitmap
    private fun zoomOutToWholeTrack() {
        val bounds = LatLngBounds.builder()
        for (polyline in pathPoints) {
            for (position in polyline) {
                bounds.include(position)
            }
        }

        //moving the camera to the created bounds
        map?.moveCamera(CameraUpdateFactory.newLatLngBounds(
            bounds.build(),
            binding.mapView.width,
            binding.mapView.height,
            (binding.mapView.height * 0.05f).toInt()
        ))
    }

    //function to end the session and save the session data to database
    private fun endSessionAndSaveToDatabase() {
        map?.snapshot { bitmap ->
            var distanceInMeters = 0
            for (polyline in pathPoints) {
                distanceInMeters += Utilities.calculateTotalPolylineDistance(polyline).toInt()
            }
            val distanceInKms = distanceInMeters / 1000f
            val timeInHours = currentTimeInMillis / 1000f / 60 / 60
            //calculating average speed in the session using totalDistance and time taken
            val averageSpeed = round(((distanceInKms / timeInHours) * 10).toDouble()) / 10f

            //calculating calories burned
            val caloriesBurned = (distanceInKms * userWeight).toInt()

            //creating session object to save in database
            val session = Session(
                bitmap,
                distanceInMeters,
                averageSpeed.toFloat(),
                caloriesBurned,
                currentTimeInMillis,
                Calendar.getInstance().timeInMillis
            )

            mainViewModel.addRun(session)
            Snackbar.make(binding.root, "Session has ended and data has been saved!", Snackbar.LENGTH_SHORT).show()
            endSession()
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