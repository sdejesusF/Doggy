package com.example.doggy.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "temp_breed")
data class TempBreedEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val imageUrl: String?,
    val group: String?,
    val origin: String?,
    val temperament: String?,
)