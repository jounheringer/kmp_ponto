package com.reringuy.clockin.utils

import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.abs

fun formatInstantToDateTime(instant: Instant): Pair<String, String> {
    val localDateTime = instant.atZone(ZoneId.systemDefault())

    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    val result = formatter.format(localDateTime)
    return Pair(result.split(" ")[0], result.split(" ")[1])
}

fun formatLocalDateToDate(localDate: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return formatter.format(localDate)
}

fun formatInstantToYearAndMonth(instant: Instant): String {
    val localDateTime = instant.atZone(ZoneId.systemDefault())

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM")
    return formatter.format(localDateTime)
}

fun formatDurationToHoursAndMinutes(duration: Duration): String {
    val totalMinutes = duration.toMinutes()
    val isNegative = totalMinutes < 0
    val absoluteMinutes = abs(totalMinutes)

    val hours = absoluteMinutes / 60
    val minutes = absoluteMinutes % 60

    val sign = if (isNegative) "-" else "+"

    return String.format("%s%02d:%02d", sign, hours, minutes)
}