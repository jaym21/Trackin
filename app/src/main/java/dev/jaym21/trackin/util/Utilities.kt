package dev.jaym21.trackin.util

import android.location.Location
import android.util.Log
import dev.jaym21.trackin.service.Polyline
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object Utilities {

    fun timeToTimerFormat(time: Long, includeMillis: Boolean = false): String {
        var millis = time
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        millis -= TimeUnit.HOURS.toMillis(hours)

        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
        millis -= TimeUnit.MINUTES.toMillis(minutes)

        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis)

        if (!includeMillis) {
            return "${if (hours < 10) "0" else ""}$hours:" +
                    "${if (minutes < 10) "0" else ""}$minutes:" +
                    "${if (seconds < 10) "0" else ""}$seconds"
        }
        millis -= TimeUnit.SECONDS.toMillis(seconds)
        millis /= 10

        return "${if (hours < 10) "0" else ""}$hours:" +
                "${if (minutes < 10) "0" else ""}$minutes:" +
                "${if (seconds < 10) "0" else ""}$seconds:" +
                "${if (millis < 10) "0" else ""}$millis"
    }

    fun timeToOverallStatsFormat(time: Long): String {
        var millis = time
        val days = TimeUnit.MILLISECONDS.toDays(millis)
        millis -= TimeUnit.DAYS.toMillis(days)

        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        millis -= TimeUnit.HOURS.toMillis(hours)

        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
        millis -= TimeUnit.MINUTES.toMillis(minutes)

        return "${if (days < 10 && days != 0L) "0" else ""}${days}d " +
                "${if (hours < 10 && hours != 0L) "0" else ""}${hours}h " +
                "${if (minutes < 10 && minutes != 0L) "0" else ""}${minutes}m"
    }

    fun calculateTotalPolylineDistance(polyline: Polyline): Float {
        var totalDistance = 0f

        for (i in 0..polyline.size - 2) {
            val position1 = polyline[i]
            val position2 = polyline[i + 1]

            val result = FloatArray(1)
            Location.distanceBetween(
                position1.latitude,
                position1.longitude,
                position2.latitude,
                position2.longitude,
                result
            )
            totalDistance += result[0]
        }
        return totalDistance
    }

    fun convertDateFormat(timestamp: Long): String {
        val sdf = SimpleDateFormat("MMM dd,yyyy", Locale.ENGLISH)
        return sdf.format(timestamp)
    }

    fun getDateFromTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd", Locale.ENGLISH)
        return sdf.format(timestamp)
    }

    fun getMonthFullName(timestamp: Long): String {
        val sdf = SimpleDateFormat("MMMM", Locale.ENGLISH)
        return sdf.format(timestamp)
    }
}