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

    @Query("SELECT * FROM run_table ORDER BY timestamp DESC")
    fun getAllSessionsOrderByDate(): LiveData<List<Session>>
}