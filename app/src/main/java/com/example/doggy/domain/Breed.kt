package com.example.doggy.domain

data class Breed(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val group: String?,
    val origin: String?,
    val temperament: String?
)