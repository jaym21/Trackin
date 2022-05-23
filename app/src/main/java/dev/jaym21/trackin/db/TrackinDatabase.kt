package dev.jaym21.trackin.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.jaym21.trackin.model.Run

@Database(entities = [Run::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TrackinDatabase: RoomDatabase() {

    abstract fun getRunDao(): RunDAO
}