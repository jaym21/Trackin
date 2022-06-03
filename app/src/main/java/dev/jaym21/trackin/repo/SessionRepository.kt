package dev.jaym21.trackin.repo

import dev.jaym21.trackin.db.SessionDAO
import dev.jaym21.trackin.db.TrackinDatabase
import dev.jaym21.trackin.model.Session
import javax.inject.Inject

class SessionRepository @Inject constructor(private val database: TrackinDatabase) {

    suspend fun insertSession(session: Session) = database.getSessionDao().insertSession(session)

    suspend fun deleteSession(session: Session) = database.getSessionDao().deleteSession(session)

    fun getAllSessionsOrderByDate() = database.getSessionDao().getAllSessionsOrderByDate()

    fun getTotalDistance() = database.getSessionDao().getTotalDistance()

    fun getTotalCaloriesBurned() = database.getSessionDao().getTotalCalories()

    fun getTotalAverageSpeed() = database.getSessionDao().getTotalAverageSpeed()

    fun getTotalSessionTime() = database.getSessionDao().getTotalSessionTime()
}