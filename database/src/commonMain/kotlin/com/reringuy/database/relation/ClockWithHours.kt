package com.reringuy.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.reringuy.database.entities.Clock
import com.reringuy.database.entities.ClockHour
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class ClockWithHours(
    @Embedded
    val clock: Clock,
    @Relation(
        parentColumn = "uid",
        entityColumn = "clockId"
    )
    val clockHours: List<ClockHour>,
) {
    companion object {
        fun new() = ClockWithHours(
            clock = Clock(0,
                kotlin.time.Clock.System.now()
                    .toLocalDateTime(TimeZone.currentSystemDefault()).date,
                null
            ),
            clockHours = emptyList()
        )
    }
}