package dev.jaym21.trackin.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import dev.jaym21.trackin.R
import dev.jaym21.trackin.ui.MainActivity
import dev.jaym21.trackin.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.EasyPermissions

typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

class TrackingService: LifecycleService() {

    private val PERMISSIONS_BELOW_Q = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private val PERMISSIONS_ABOVE_Q = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
    )
    var isFirstRun = true
    private val sessionTimeInSeconds: MutableLiveData<Long> = MutableLiveData()
    private var isTimerEnabled = false
    private var sessionTime = 0L
    private var lapTime = 0L
    private var timeStarted = 0L
    private var lastSecondTimeStamp = 0L
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    companion object {
        val sessionTimeInMillis: MutableLiveData<Long> = MutableLiveData()
        val isTracking: MutableLiveData<Boolean> = MutableLiveData()
        val pathPoints: MutableLiveData<Polylines> = MutableLiveData()
    }

    override fun onCreate() {
        super.onCreate()

        initValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        //getting current isTracking state and updating the user's location update request
        isTracking.observe(this, Observer {
            updateLocationTracking(it)
        })
    }

    private fun initValues() {
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
        sessionTimeInMillis.postValue(0L)
        sessionTimeInSeconds.postValue(0L)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Constants.ACTION_START_OR_RESUME -> {
                if (isFirstRun) {
                    startForegroundService()
                    isFirstRun = false
                } else{
                    startTimer()
                }
            }

            Constants.ACTION_PAUSE -> {
                isTracking.postValue(false)
                isTimerEnabled = false
            }

            Constants.ACTION_STOP -> {
                Log.d("TAGYOYO", "onStartCommand: ACTION_STOP")
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {
        startTimer()
        isTracking.postValue(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(notificationManager)

        val notificationClickIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java).also {
                it.action = Constants.ACTION_SHOW_SESSION_FRAGMENT
            },
            PendingIntent.FLAG_UPDATE_CURRENT
        )


        //TODO: change to app icon
        val notificationBuilder = NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Trackin")
            .setContentText("00:00:00")
            .setContentIntent(notificationClickIntent)
            .setAutoCancel(false)
            .setOngoing(true)

        startForeground(Constants.NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun startTimer() {
        addEmptyPolyline()
        isTracking.postValue(true)
        isTimerEnabled = true
        timeStarted = System.currentTimeMillis()

        CoroutineScope(Dispatchers.Main).launch {
            while (isTracking.value!!) {
                //time difference between now and time session started
                lapTime = System.currentTimeMillis() - timeStarted
                //updating session time in millis total previous time plus new lap time
                sessionTimeInMillis.postValue(sessionTime + lapTime)
                //checking if the session time in millis is more than or equal to previous second passed plus one second then increasing a second in session time in seconds
                if (sessionTimeInMillis.value!! >= lastSecondTimeStamp + 1000L) {
                    sessionTimeInSeconds.postValue(sessionTimeInSeconds.value!! +  1)
                    lastSecondTimeStamp += 1000L
                }
                delay(Constants.TIMER_UPDATE_INTERVAL)
            }
            //updating the total session time by adding the latest lap time
            sessionTime += lapTime
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean) {
        if (isTracking) {
            if (checkIfPermissionsGranted()) {
                //if currently tracking and location permissions are available, then requesting user's location update
                val locationRequest = LocationRequest.create().apply {
                    interval = Constants.LOCATION_UPDATE_INTERVAL
                    fastestInterval = Constants.FASTEST_LOCATION_INTERVAL
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
            }
        } else {
            //removing request for location update
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if (isTracking.value!!) {
                result.locations.let { locations ->
                    for (location in locations) {
                        addPathPoint(location)
                        Log.d("TAGYOYO", "onLocationResult:latitude ${location.latitude} longitude ${location.longitude}")
                    }
                }
            }
        }
    }

    private fun addEmptyPolyline() {
        pathPoints.value?.apply {
            add(mutableListOf())
            pathPoints.postValue(this)
        } ?: pathPoints.postValue(mutableListOf(mutableListOf()))
    }

    private fun addPathPoint(location: Location?) {
        location?.let {
            val positionLatLng = LatLng(location.latitude, location.longitude)
            pathPoints.value?.apply {
                last().add(positionLatLng)
                pathPoints.postValue(this)
            }
        }
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O)
            return
        val channel = NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID, Constants.NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW)
        notificationManager.createNotificationChannel(channel)
    }

    private fun checkIfPermissionsGranted(): Boolean =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.hasPermissions(this,*PERMISSIONS_BELOW_Q)
        } else {
            EasyPermissions.hasPermissions(this,*PERMISSIONS_ABOVE_Q)
        }
}