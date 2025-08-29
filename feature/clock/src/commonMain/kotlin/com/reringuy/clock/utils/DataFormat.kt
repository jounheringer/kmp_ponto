@file:OptIn(ExperimentalTime::class)

package com.reringuy.clock.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.math.abs
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.toDuration

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
    val absoluteDuration = abs(totalMinutes).toDuration(DurationUnit.MINUTES)

    // Desestrutura a duração dentro da lambda para evitar a ambiguidade
    val (hours, minutes) = absoluteDuration.toComponents { hours, minutes, _, _ ->
        Pair(hours, minutes)
    }

    val sign = if (isNegative) "-" else ""

    val formattedHours = hours.toString().padStart(2, '0')
    val formattedMinutes = minutes.toString().padStart(2, '0')

    return "$sign$formattedHours:$formattedMinutes"
}