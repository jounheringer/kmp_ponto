package com.reringuy.database.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Entity(
    tableName = "ponto_hora",
    indices = [Index(value = ["clockId"])],
    foreignKeys = [
        ForeignKey(
            entity = Clock::class,
            parentColumns = ["uid"],
            childColumns = ["clockId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ClockHour (
    @PrimaryKey(autoGenerate = true)
    val uid: Long,
    val date: Instant,
    val type: ClockHourType,
    val clockId: Long
)


enum class ClockHourType {
    ENTRADA,
    SAIDA
}