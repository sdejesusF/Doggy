package com.example.doggy.data

import androidx.paging.*
import com.example.doggy.data.local.dao.BreedDao
import com.example.doggy.domain.Breed
import com.example.doggy.domain.Sort
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface BreedStore {
    fun getBreed(id: String): Flow<Breed>
    fun getBreedsPage(scope: CoroutineScope, sorting: Sort): Flow<PagingData<Breed>>
    fun searchBreeds(searchTerm: String): Flow<List<Breed>>
}

val emptyBreed = Breed(id = "", name = "", group = "", origin = "", temperament = "", imageUrl = "")

class BreedStoreDefault @Inject constructor(
    private val breedDao: BreedDao
): BreedStore {

    override fun getBreed(id: String): Flow<Breed> {
        return breedDao.getBreed(id)
            .map { be -> be.toDomain() }
            .catch {
                emit(emptyBreed)
            }
    }

    override fun getBreedsPage(scope: CoroutineScope, sorting: Sort): Flow<PagingData<Breed>> {
        return Pager(PagingConfig(pageSize = 20)) {
            when (sorting) {
                Sort.ASC -> breedDao.getBreedsPagerAsc()
                Sort.DESC -> breedDao.getBreedsPagerDesc()
            }
        }.flow
        .map { pd -> pd.map { be -> be.toDomain() }}
        .cachedIn(scope = scope)
        .catch { emit(PagingData.from(listOf())) }
    }

    override fun searchBreeds(searchTerm: String): Flow<List<Breed>> {
        return breedDao.searchBreeds(searchTerm = searchTerm)
            .map { beList -> beList.map { be -> be.toDomain() }}
            .catch {
                emit(listOf())
            }
    }
}