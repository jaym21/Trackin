package dev.jaym21.trackin.worker

import android.content.Context
import android.content.SharedPreferences
import androidx.hilt.work.HiltWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.jaym21.trackin.util.Constants
import dev.jaym21.trackin.util.Utilities
import java.util.concurrent.TimeUnit

@HiltWorker
class DailyGoalWorker @AssistedInject constructor(private val sharedPreferences: SharedPreferences, @Assisted context: Context, @Assisted params: WorkerParameters): Worker(context, params) {

    override fun doWork(): Result {

        sharedPreferences.edit()
            .putFloat(Constants.DISTANCE_GOAL_COMPLETED, 0f)
            .putInt(Constants.CALORIES_GOAL_COMPLETED, 0)
            .apply()

        enqueueNextWorker()

        return Result.success()
    }

    private fun enqueueNextWorker() {
        val delay = Utilities.getNextDayTimestamp() - System.currentTimeMillis()
        if (delay > 0) {
            val dailyWork = OneTimeWorkRequest.Builder(DailyGoalWorker::class.java)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build()

            WorkManager.getInstance(applicationContext).enqueue(dailyWork)
        }
    }
}