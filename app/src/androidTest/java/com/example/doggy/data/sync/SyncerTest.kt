package com.example.doggy.data.sync

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.doggy.data.BaseDatabaseTest
import com.example.doggy.data.network.BreedDto
import com.example.doggy.data.network.DogApi
import com.example.doggy.data.network.ImageDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SyncerTest: BaseDatabaseTest() {

    @Test
    fun synExecuteTest() = runBlocking {
        val dogApi = mockk<DogApi>()
        val breed1 = BreedDto(
            id = "1",
            name = "name",
            origin = "",
            temperament = "",
            image = ImageDto(id = "1", url = ""),
            breedGroup = "",
            description = "",
            lifeSpan = ""
        )
        coEvery { dogApi.getAllBreeds(0, 100)} returns listOf(breed1)
        coEvery { dogApi.getAllBreeds(1, 100)} returns listOf()
        val syncer = SyncerDefault(breedDao = dao, dogApi = dogApi)
        syncer.execute()
        val result = dao.getBreeds(0, 100).first()
        assertThat(result.size).isEqualTo(1)
    }
}