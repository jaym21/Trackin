package dev.jaym21.trackin.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.jaym21.trackin.db.TrackinDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): TrackinDatabase =
        Room.databaseBuilder(application, TrackinDatabase::class.java, "trackin_database")
            .build()
}