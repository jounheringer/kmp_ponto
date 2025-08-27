package com.reringuy.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.reringuy.database.dao.ClockDao
import com.reringuy.database.entities.Clock
import com.reringuy.database.entities.ClockHour

@Database(
    entities = [
        Clock::class,
        ClockHour::class
    ], version = 1
)
@TypeConverters(Converters::class)
@ConstructedBy(PontoDatabaseInitializer::class)
abstract class PontoDatabase() : RoomDatabase() {
    abstract fun clockDao(): ClockDao
}

@Suppress("KotlinNoActualForExpect")
expect object PontoDatabaseInitializer : RoomDatabaseConstructor<PontoDatabase> {
    override fun initialize(): PontoDatabase
}