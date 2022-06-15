package dev.jaym21.trackin.util

import dev.jaym21.trackin.R

object Constants {

    const val DATABASE_NAME = "trackin_database"
    const val SHARED_PREFERENCES_TRACKIN = "shared_preferences_trackin"
    const val LOCATION_REQUEST_CODE = 100
    const val LOCATION_UPDATE_INTERVAL = 5000L
    const val FASTEST_LOCATION_INTERVAL = 2000L
    const val POLYLINE_WIDTH = 8f
    const val POLYLINE_COLOR = R.color.blue_200
    const val DEFAULT_MAP_ZOOM = 15f
    const val IS_FIRST_RUN = "is_first_run"
    const val USER_NAME = "user_name"
    const val USER_WEIGHT = "user_weight"
    const val DISTANCE_GOAL = "distance_goal"
    const val DISTANCE_GOAL_COMPLETED = "distance_goal_completed"
    const val KEY_SESSION = "key_session"

    //service
    const val ACTION_START_OR_RESUME = "action_start_or_resume"
    const val ACTION_PAUSE = "action_pause"
    const val ACTION_STOP = "action_stop"
    const val TIMER_UPDATE_INTERVAL = 50L

    //notification
    const val NOTIFICATION_ID = 1
    const val NOTIFICATION_CHANNEL_ID = "tracking_channel"
    const val NOTIFICATION_CHANNEL_NAME = "tracking"
    const val ACTION_SHOW_SESSION_FRAGMENT = "action_show_session_fragment"
}