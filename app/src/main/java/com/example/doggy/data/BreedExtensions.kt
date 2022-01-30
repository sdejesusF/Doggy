package com.example.doggy.data

import com.example.doggy.data.local.entity.BreedEntity
import com.example.doggy.data.local.entity.TempBreedEntity
import com.example.doggy.data.network.BreedDto
import com.example.doggy.domain.Breed

fun BreedEntity.toDomain(): Breed = Breed(id = id, name = name, imageUrl = imageUrl, group = group, origin = origin, temperament = temperament)

fun BreedDto.toTempBreedEntity(): TempBreedEntity = TempBreedEntity(id = id, name = name, imageUrl = image?.url, group = breedGroup, origin = origin, temperament = temperament)