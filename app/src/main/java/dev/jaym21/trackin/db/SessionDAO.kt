package dev.jaym21.trackin.db

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.jaym21.trackin.model.Session

@Dao
interface SessionDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: Session)

    @Delete
    suspend fun deleteSession(session: Session)

    @Query("SELECT * FROM session_table ORDER BY timestamp DESC")
    fun getAllSessionsOrderByDate(): LiveData<List<Session>>

    @Query("SELECT SUM(distanceInMeters) FROM session_table")
    fun getTotalDistance(): LiveData<Int>

    @Query("SELECT SUM(caloriesBurned) FROM session_table")
    fun getTotalCalories(): LiveData<Int>

    @Query("SELECT SUM(avgSpeedInKMH) FROM session_table")
    fun getTotalAverageSpeed(): LiveData<Float>

    @Query("SELECT SUM(sessionTimeInMillis) FROM session_table")
    fun getTotalSessionTime(): LiveData<Int>
}