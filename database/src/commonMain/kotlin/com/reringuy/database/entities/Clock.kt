package com.reringuy.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

@Entity(
    tableName = "pontos",
    indices = [Index(value = ["data_inicio"], unique = true)]
)
data class Clock(
    @PrimaryKey(autoGenerate = true)
    var uid: Long,
    @ColumnInfo(name = "data_inicio")
    val dateStart: LocalDate,
    @ColumnInfo(name = "data_fim")
    val dateFinish: LocalDate?
)