package com.reringuy.database

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class Converters {
    @TypeConverter
    fun localDateToString(value: LocalDate?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun stringToLocalDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }

    @TypeConverter
    fun localDateTimeToString(value: LocalDateTime?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun stringToLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @OptIn(ExperimentalTime::class)
    @TypeConverter
    fun instantToLong(value: Instant?): Long? {
        return value?.toEpochMilliseconds()
    }

    @OptIn(ExperimentalTime::class)
    @TypeConverter
    fun longToInstant(value: Long?): Instant? {
        return value?.let { Instant.fromEpochMilliseconds(it) }
    }
}