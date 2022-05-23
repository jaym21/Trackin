package dev.jaym21.trackin.repo

import dev.jaym21.trackin.db.RunDAO
import dev.jaym21.trackin.model.Run
import javax.inject.Inject

class RunRepository @Inject constructor(private val runDAO: RunDAO) {

    suspend fun insertRun(run: Run) = runDAO.insertRun(run)

    suspend fun deleteRun(run: Run) = runDAO.deleteRun(run)

    fun getAllRunsOrderByDate() = runDAO.getAllRunsOrderByDate()
}