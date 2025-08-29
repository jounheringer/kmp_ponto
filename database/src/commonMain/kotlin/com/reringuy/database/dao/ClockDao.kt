package com.reringuy.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.reringuy.database.entities.Clock
import com.reringuy.database.entities.ClockHour
import com.reringuy.database.relation.ClockWithHours
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Dao
interface ClockDao {
    @Transaction
    @Query("SELECT * FROM pontos ORDER BY data_inicio DESC")
    suspend fun getAll(): List<ClockWithHours>

    @Transaction
    @Query("SELECT * FROM pontos WHERE data_inicio = :date")
    suspend fun getClockByDate(date: Instant): ClockWithHours?

    @Transaction
    @Query("SELECT * FROM pontos WHERE data_inicio = :date")
    suspend fun getClockByDateIn(date: Instant): ClockWithHours?

    @Transaction
    @Query("SELECT * FROM pontos WHERE STRFTIME('%Y-%m', data_inicio) = :yearAndMonth")
    suspend fun getClocksByMonth(yearAndMonth: String): List<ClockWithHours>

    @Upsert
    @OptIn(ExperimentalTime::class)
    suspend fun insertClock(clock: Clock): Long

    @Upsert
    @OptIn(ExperimentalTime::class)
    suspend fun insertAllClockHours(clockHour: List<ClockHour>)

    @Upsert
    @OptIn(ExperimentalTime::class)
    suspend fun insertClockHour(clockHour: ClockHour): Long
}