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
data class ClockHour(
    @PrimaryKey(autoGenerate = true)
    var uid: Long,
    val date: Instant,
    val type: ClockHourType,
    val clockId: Long
) {
    companion object {
        fun new(clockId: Long, type: ClockHourType = ClockHourType.ENTRADA) = ClockHour(0, Instant.now(), type, clockId)
    }
}


enum class ClockHourType(val label: String) {
    ENTRADA("Entrada"),
    SAIDA("Saída")
}