package com.reringuy.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

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
        fun new(clockId: Long, type: ClockHourType = ClockHourType.ENTRADA) = ClockHour(0, kotlin.time.Clock.System.now(), type, clockId)
    }
}


enum class ClockHourType(val label: String) {
    ENTRADA("Entrada"),
    SAIDA("Saída")
}