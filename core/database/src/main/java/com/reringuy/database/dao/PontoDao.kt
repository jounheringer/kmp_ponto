package com.reringuy.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.reringuy.database.dto.ClockWithHours
import com.reringuy.database.models.Clock
import com.reringuy.database.models.ClockHour
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface PontoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertClock(clock: Clock)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertClockHour(clockHour: ClockHour)

    @Transaction
    @Query("SELECT * FROM pontos WHERE data_inicio = :date")
    fun getClockByDate(date: LocalDate): Flow<ClockWithHours?>
}