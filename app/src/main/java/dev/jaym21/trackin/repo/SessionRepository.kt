package dev.jaym21.trackin.repo

import dev.jaym21.trackin.db.SessionDAO
import dev.jaym21.trackin.model.Session
import javax.inject.Inject

class SessionRepository @Inject constructor(private val sessionDAO: SessionDAO) {

    suspend fun insertSession(session: Session) = sessionDAO.insertSession(session)

    suspend fun deleteSession(session: Session) = sessionDAO.deleteSession(session)

    fun getAllSessionsOrderByDate() = sessionDAO.getAllSessionsOrderByDate()
}