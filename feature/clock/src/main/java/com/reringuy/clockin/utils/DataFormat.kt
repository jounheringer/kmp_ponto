@file:OptIn(ExperimentalTime::class)

package com.reringuy.clockin.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun formatInstantToDateTime(instant: Instant): Pair<String, String> {
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

    val formatter = LocalDateTime.Format {
        day()
        char('/')
        monthNumber()
        char('/')
        year()
        char(' ')
        hour()
        char(':')
        minute()
    }

    val result = localDateTime.format(formatter)
    return Pair(result.split(" ")[0], result.split(" ")[1])
}

fun formatLocalDateToDate(localDate: LocalDate): String {
    val formatter = LocalDate.Format {
        day()
        char('/')
        monthNumber()
        char('/')
        year()
    }
    return localDate.format(formatter)
}

fun formatInstantToYearAndMonth(instant: Instant): String {
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

    val formatter = LocalDateTime.Format {
        year()
        char('-')
        monthNumber()
    }
    return localDateTime.format(formatter)
}

fun formatDurationToHoursAndMinutes(duration: Duration): String {
    val totalMinutes = duration.inWholeMinutes
    val isNegative = totalMinutes < 0
    val absoluteMinutes = kotlin.math.abs(totalMinutes)

    val hours = absoluteMinutes / 60
    val minutes = absoluteMinutes % 60

    val sign = if (isNegative) "-" else ""

    return String.format("%s%02d:%02d", sign, hours, minutes)
}