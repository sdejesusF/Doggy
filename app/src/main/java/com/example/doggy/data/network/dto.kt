package com.example.doggy.data.network

import com.google.gson.annotations.SerializedName

data class BreedDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("temperament")
    val temperament: String,
    @SerializedName("life_span")
    val lifeSpan: String,
    @SerializedName("origin")
    val origin: String,
    @SerializedName("breed_group")
    val breedGroup: String,
    @SerializedName("image")
    val image: ImageDto,
)

data class ImageDto(
    @SerializedName("id") val id: String,
    @SerializedName("url") val url: String
)