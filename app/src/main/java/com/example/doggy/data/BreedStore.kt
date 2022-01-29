package com.example.doggy.data

import com.example.doggy.data.local.dao.BreedDao
import com.example.doggy.domain.Breed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface BreedStore {
    suspend fun getBreeds(page: Int, limit: Int): Flow<List<Breed>>
    suspend fun getBreed(id: String): Flow<Breed>
    suspend fun searchBreeds(searchTerm: String): Flow<List<Breed>>
}

val emptyBreed = Breed(id = "", name = "", group = "", origin = "", temperament = "", imageUrl = "")

class BreedStoreDefault @Inject constructor(
    private val breedDao: BreedDao
): BreedStore {
    override suspend fun getBreeds(page: Int, limit: Int): Flow<List<Breed>> {
        return breedDao.getBreeds(page = page * limit, limit = limit)
            .map { breedDto ->
                breedDto.map { breed ->
                    Breed(
                        id = breed.id,
                        name = breed.name,
                        imageUrl = breed.imageUrl,
                        group = breed.group,
                        origin = breed.origin,
                        temperament = breed.temperament
                    )
                }
            }
            .catch {
                emit(listOf())
            }
    }

    override suspend fun getBreed(id: String): Flow<Breed> {
        return breedDao.getBreed(id)
            .map { breed ->
                Breed(
                    id = breed.id,
                    name = breed.name,
                    imageUrl = breed.imageUrl,
                    group = breed.group,
                    origin = breed.origin,
                    temperament = breed.temperament,
                )
            }
            .catch {
                emit(emptyBreed)
            }

    }

    override suspend fun searchBreeds(searchTerm: String): Flow<List<Breed>> {
        return breedDao.searchBreeds(searchTerm = searchTerm)
            .map { breedDto ->
                breedDto.map { breed ->
                    Breed(
                        id = breed.id,
                        name = breed.name,
                        imageUrl = breed.imageUrl,
                        group = breed.group,
                        origin = breed.origin,
                        temperament = breed.temperament
                    )
                }
            }
            .catch {
                emit(listOf())
            }
    }
}