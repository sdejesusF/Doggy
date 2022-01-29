package com.example.doggy.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface DogApi {
    @GET("/v1/breeds")
    suspend fun getAllBreeds(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): List<BreedDto>
}