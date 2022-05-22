package dev.jaym21.trackin.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "run_table")
data class Run (
    var runImage: Bitmap? = null,
    var distanceInMeters: Int = 0,
    var avgSpeedInKMH: Float = 0F,
    var caloriesBurned: Int = 0,
    var runTimeInMillis: Long = 0L,
    var timestamp: Long = 0L
) {
        @PrimaryKey(autoGenerate = true)
        var id: Int? = null
}