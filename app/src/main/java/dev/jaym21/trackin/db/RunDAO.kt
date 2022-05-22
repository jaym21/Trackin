package dev.jaym21.trackin.db

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.jaym21.trackin.model.Run

@Dao
interface RunDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run: Run)

    @Delete
    suspend fun deleteRun(run: Run)

    @Query("SELECT * FROM run_table ORDER BY timestamp DESC")
    fun getAllRunsOrderByDate(): LiveData<List<Run>>
}