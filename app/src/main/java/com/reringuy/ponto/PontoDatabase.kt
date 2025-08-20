package com.reringuy.ponto

import androidx.room.Database
import androidx.room.RoomDatabase
import com.reringuy.ponto.dao.PontoDao
import com.reringuy.ponto.models.Ponto

@Database(
    entities = [
        Ponto::class
    ],
    version = 1
)
abstract class PontoDatabase : RoomDatabase() {
    abstract fun pontoDao(): PontoDao
}