package com.example.doggy.data.sync

import com.example.doggy.data.local.dao.BreedDao
import com.example.doggy.data.local.entity.TempBreedEntity
import com.example.doggy.data.network.DogApi
import com.example.doggy.data.toTempBreedEntity
import javax.inject.Inject

interface Syncer {
    suspend fun execute()
}

class SyncerDefault @Inject constructor(
    private val breedDao: BreedDao,
    private val dogApi: DogApi,
): Syncer {
    override suspend fun execute() {
        var page = 0
        val size = 100
        do {
            val entityBulk = dogApi.getAllBreeds(page = page, limit = size)
                .map { b -> b.toTempBreedEntity() }
            breedDao.insertTempBreeds(entityBulk)
            page++
        } while (entityBulk.isNotEmpty())
        breedDao.updateBreedsFromTemp()
    }
}