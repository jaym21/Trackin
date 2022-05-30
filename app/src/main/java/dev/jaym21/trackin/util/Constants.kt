package dev.jaym21.trackin.util

import android.graphics.Color

object Constants {

    const val DATABASE_NAME = "trackin_database"
    const val LOCATION_REQUEST_CODE = 100
    const val LOCATION_UPDATE_INTERVAL = 5000L
    const val FASTEST_LOCATION_INTERVAL = 2000L
    const val POLYLINE_WIDTH = 8f
    const val POLYLINE_COLOR = Color.BLUE
    const val DEFAULT_MAP_ZOOM = 15f

    //service
    const val ACTION_START_OR_RESUME = "action_start_or_resume"
    const val ACTION_PAUSE = "action_pause"
    const val ACTION_STOP = "action_stop"

    //notification
    const val NOTIFICATION_ID = 1
    const val NOTIFICATION_CHANNEL_ID = "tracking_channel"
    const val NOTIFICATION_CHANNEL_NAME = "tracking"
    const val ACTION_SHOW_SESSION_FRAGMENT = "action_show_session_fragment"
}