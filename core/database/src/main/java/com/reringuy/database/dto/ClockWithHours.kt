package com.reringuy.database.dto

import androidx.room.Embedded
import androidx.room.Relation
import com.reringuy.database.models.Clock
import com.reringuy.database.models.ClockHour
import java.time.LocalDate

data class ClockWithHours(
    @Embedded
    val clock: Clock,
    @Relation(
        parentColumn = "uid",
        entityColumn = "clockId"
    )
    val clockHours: List<ClockHour>
) {
    companion object {
        fun new() = ClockWithHours(
            clock = Clock(0, LocalDate.now(), null),
            clockHours = emptyList()
        )
    }
}
