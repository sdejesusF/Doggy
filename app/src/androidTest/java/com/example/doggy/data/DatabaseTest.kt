package com.example.doggy.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.doggy.data.local.entity.TempBreedEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseTest : BaseDatabaseTest() {

    @Test
    fun breedDaoTest() = runBlocking {
        val tempBreeds = listOf(
            TempBreedEntity(id = "1", name = "Test", imageUrl = "", temperament = "", origin = "", group = ""),
            TempBreedEntity(id = "2", name = "Test2", imageUrl = "", temperament = "", origin = "", group = ""),
            TempBreedEntity(id = "3", name = "V", imageUrl = "", temperament = "", origin = "", group = "")
        )
        dao.insertTempBreeds(tempBreeds)
        dao.updateBreedsFromTemp()
        val breed = dao.getBreed(id = "1").first()

        assertThat(breed.id).matches("1")

        val search = dao.searchBreeds(searchTerm = "T").first()
        assertThat(search.size).isEqualTo(2)
        assertThat(search[0].name).matches("Test")
        assertThat(search[1].name).matches("Test2")

        val list0 = dao.getBreeds(0, 2).first()
        assertThat(list0.size).isEqualTo(2)
        assertThat(list0[0].name).matches("Test")
        assertThat(list0[1].name).matches("Test2")

        val list1 = dao.getBreeds(2, 2).first()
        assertThat(list1.size).isEqualTo(1)
        assertThat(list1[0].name).matches("V")
    }
}