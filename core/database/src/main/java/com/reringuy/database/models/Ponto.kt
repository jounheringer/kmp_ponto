package com.reringuy.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pontos")
data class Ponto(
    @PrimaryKey(autoGenerate = true)
    val uid: Long,
)
