package com.example.doggy.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// if dataset was larger could have been use a fst table instead
@Entity(tableName = "breed", indices = [Index(value = ["name"])])
data class BreedEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val imageUrl: String?,
    val group: String?,
    val origin: String?,
    val temperament: String?,
)