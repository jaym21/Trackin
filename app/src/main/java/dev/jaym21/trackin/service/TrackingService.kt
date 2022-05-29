package dev.jaym21.trackin.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import dev.jaym21.trackin.R
import dev.jaym21.trackin.ui.MainActivity
import dev.jaym21.trackin.util.Constants

typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

class TrackingService: LifecycleService() {

    var isFirstRun = true

    companion object {
        val isTracking: MutableLiveData<Boolean> = MutableLiveData()
        val pathPoints: MutableLiveData<Polylines> = MutableLiveData()
    }

    private fun initValues() {
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Constants.ACTION_START_OR_RESUME -> {
                if (isFirstRun) {
                    startForegroundService()
                    isFirstRun = false
                } else{
                    Log.d("TAGYOYO", "onStartCommand: RESUME")
                }
            }

            Constants.ACTION_PAUSE -> {
                Log.d("TAGYOYO", "onStartCommand: ACTION_PAUSE")
            }

            Constants.ACTION_STOP -> {
                Log.d("TAGYOYO", "onStartCommand: ACTION_STOP")
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {

        addEmptyPolyline()

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

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O)
            return
        val channel = NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID, Constants.NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW)
        notificationManager.createNotificationChannel(channel)
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
}