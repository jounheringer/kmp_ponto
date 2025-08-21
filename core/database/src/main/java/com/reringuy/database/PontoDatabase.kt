package com.reringuy.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.reringuy.database.dao.PontoDao
import com.reringuy.database.models.Clock
import com.reringuy.database.models.ClockHour

@Database(
    entities = [
        Clock::class,
        ClockHour::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class PontoDatabase : RoomDatabase() {
    abstract fun pontoDao(): PontoDao
}