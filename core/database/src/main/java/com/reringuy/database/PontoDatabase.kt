package com.reringuy.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.reringuy.database.dao.PontoDao
import com.reringuy.database.models.Ponto

@Database(
    entities = [
        Ponto::class
    ],
    version = 1
)
abstract class PontoDatabase : RoomDatabase() {
    abstract fun pontoDao(): PontoDao
}