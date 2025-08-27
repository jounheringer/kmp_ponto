package com.reringuy.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.reringuy.database.entities.Clock
import com.reringuy.database.entities.ClockHour
import com.reringuy.database.relation.ClockWithHours
import kotlinx.coroutines.flow.Flow
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Dao
interface ClockDao {
    @Transaction
    @Query("SELECT * FROM pontos ORDER BY data_inicio DESC")
    fun getAll(): Flow<List<ClockWithHours>>

    @Transaction
    @Query("SELECT * FROM pontos WHERE data_inicio = :date")
    fun getClockByDate(date: Instant): Flow<ClockWithHours?>

    @Transaction
    @Query("SELECT * FROM pontos WHERE data_inicio = :date")
    fun getClockByDateIn(date: Instant): Flow<ClockWithHours?>

    @Transaction
    @Query("SELECT * FROM pontos WHERE STRFTIME('%Y-%m', data_inicio) = :yearAndMonth")
    fun getClocksByMonth(yearAndMonth: String): Flow<List<ClockWithHours>>

    @Upsert
    @OptIn(ExperimentalTime::class)
    fun insertClock(clock: Clock): Long

    @Upsert
    @OptIn(ExperimentalTime::class)
    fun insertAllClockHours(clockHour: List<ClockHour>)

    @Upsert
    @OptIn(ExperimentalTime::class)
    fun insertClockHour(clockHour: ClockHour): Long
}