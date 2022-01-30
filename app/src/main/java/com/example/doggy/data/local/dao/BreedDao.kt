package com.example.doggy.data.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.example.doggy.data.local.entity.BreedEntity
import com.example.doggy.data.local.entity.TempBreedEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class BreedDao {
    @Query("SELECT * FROM breed LIMIT :page,:limit")
    abstract fun getBreeds(page: Int, limit: Int): Flow<List<BreedEntity>>

    @Query("SELECT * FROM breed ORDER BY name ASC")
    abstract fun getBreedsPagerAsc(): PagingSource<Int, BreedEntity>

    @Query("SELECT * FROM breed ORDER BY name DESC")
    abstract fun getBreedsPagerDesc(): PagingSource<Int, BreedEntity>

    @Query("SELECT * FROM breed WHERE id = :id")
    abstract fun getBreed(id: String): Flow<BreedEntity>

    @Query("SELECT * FROM breed WHERE name LIKE '%' || :searchTerm || '%'")
    abstract fun searchBreeds(searchTerm: String): Flow<List<BreedEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertTempBreeds(breeds: List<TempBreedEntity>)

    @Query("DELETE FROM breed")
    abstract fun deleteBreeds()

    @Query("DELETE FROM temp_breed")
    abstract fun clearTempBreeds()

    @Query("INSERT OR REPLACE INTO breed SELECT * FROM temp_breed")
    abstract fun replaceBreedsFromTemp()

    @Transaction
    open fun updateBreedsFromTemp()
    {
        deleteBreeds()
        replaceBreedsFromTemp()
        clearTempBreeds()
    }
}