package dev.jaym21.trackin.model

data class OverallStats(
    var totalDistance: Int,
    var totalCaloriesBurned: Int,
    var totalSessionTime: Long,
    var totalAverageSpeed: Float
)
