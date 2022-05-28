package dev.jaym21.trackin.service

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleService
import dev.jaym21.trackin.util.Constants

class TrackingService: LifecycleService() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Constants.ACTION_START_OR_RESUME -> {
                Log.d("TAGYOYO", "onStartCommand: ACTION_START_OR_RESUME")
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
}