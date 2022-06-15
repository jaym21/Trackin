package dev.jaym21.trackin.di

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.jaym21.trackin.db.TrackinDatabase
import dev.jaym21.trackin.repo.SessionRepository
import dev.jaym21.trackin.util.Constants
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): TrackinDatabase =
        Room.databaseBuilder(application, TrackinDatabase::class.java, Constants.DATABASE_NAME)
            .build()

    @Provides
    @Singleton
    fun provideRepository(database: TrackinDatabase): SessionRepository =
        SessionRepository(database)

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext application: Context) =
        application.getSharedPreferences(Constants.SHARED_PREFERENCES_TRACKIN, MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideUserName(sharedPreferences: SharedPreferences) =
        sharedPreferences.getString(Constants.USER_NAME, "") ?: ""

    @Provides
    @Singleton
    fun provideUserWeight(sharedPreferences: SharedPreferences) =
        sharedPreferences.getFloat(Constants.USER_WEIGHT, 70f)

    @Provides
    @Singleton
    fun provideDistanceGoal(sharedPreferences: SharedPreferences) =
        sharedPreferences.getInt(Constants.DISTANCE_GOAL, 1)

    @Provides
    @Singleton
    fun provideDistanceGoalCompleted(sharedPreferences: SharedPreferences) =
        sharedPreferences.getFloat(Constants.DISTANCE_GOAL_COMPLETED, 0F)

    @Provides
    @Singleton
    fun provideIsFirstRun(sharedPreferences: SharedPreferences) =
        sharedPreferences.getBoolean(Constants.IS_FIRST_RUN, true)
}