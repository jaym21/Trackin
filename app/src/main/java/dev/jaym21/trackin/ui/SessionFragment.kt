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
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import dev.jaym21.trackin.R
import dev.jaym21.trackin.databinding.FragmentSessionBinding
import dev.jaym21.trackin.service.Polyline
import dev.jaym21.trackin.service.TrackingService
import dev.jaym21.trackin.util.Constants
import dev.jaym21.trackin.util.Utilities

class SessionFragment : Fragment() {

    private var _binding: FragmentSessionBinding? = null
    private val binding: FragmentSessionBinding
        get() = _binding!!
    private var map: GoogleMap? = null
    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()
    private var currentTimeInMillis = 0L

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
            val timerFormat = Utilities.formatTimestampToTimer(it, true)
            binding.tvTimer.text = timerFormat
        }
    }

    //update button state according to current isTracking state
    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        if (isTracking) {
            binding.fabPlayPause.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_pause))
            binding.ivStopSession.visibility = View.GONE
        } else {
            binding.ivCancelSession.visibility = View.VISIBLE
            binding.fabPlayPause.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_play))
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
        val builder = AlertDialog.Builder(requireContext(), R.style.CancelSessionAlertDialog).create()

        val view = layoutInflater.inflate(R.layout.cancel_session_layout, null)
        val noButton: TextView = view.findViewById(R.id.tvNoCancelDialog)
        val yesButton: TextView = view.findViewById(R.id.tvYesCancelDialog)

        builder.setView(view)
        builder.setCanceledOnTouchOutside(false)

        yesButton.setOnClickListener {
            cancelSession()
            Snackbar.make(binding.root, "Session has been cancelled!", Snackbar.LENGTH_SHORT).show()
            builder.dismiss()
        }
        noButton.setOnClickListener {
            builder.dismiss()
        }
        builder.show()
    }

    //function to cancel the session and not save the session data to database
    private fun cancelSession() {
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