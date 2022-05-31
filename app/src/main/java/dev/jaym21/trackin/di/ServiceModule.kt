package dev.jaym21.trackin.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import dev.jaym21.trackin.R
import dev.jaym21.trackin.ui.MainActivity
import dev.jaym21.trackin.util.Constants

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @Provides
    @ServiceScoped
    fun provideFusedLocationProviderClient(@ApplicationContext application: Context) =
        FusedLocationProviderClient(application)


    @Provides
    @ServiceScoped
    fun provideMainActivityPendingIntent(@ApplicationContext application: Context) =
        PendingIntent.getActivity(
            application,
            0,
            Intent(application, MainActivity::class.java).also {
                it.action = Constants.ACTION_SHOW_SESSION_FRAGMENT
            },
            PendingIntent.FLAG_UPDATE_CURRENT
        )

    @Provides
    @ServiceScoped
    fun provideBaseNotificationBuilder(@ApplicationContext application: Context, notificationClickIntent: PendingIntent) =
        NotificationCompat.Builder(application, Constants.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)//TODO: change to app icon
            .setContentTitle("Trackin")
            .setContentText("00:00:00")
            .setContentIntent(notificationClickIntent)
            .setAutoCancel(false)
            .setOngoing(true)
}