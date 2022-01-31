package com.example.doggy.ui.breeddetail

import androidx.lifecycle.SavedStateHandle
import com.example.doggy.MainCoroutineRule
import com.example.doggy.data.BreedStore
import com.example.doggy.domain.Breed
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

class BreedDetailViewModelDefaultTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = MainCoroutineRule()

    @Test
    fun viewModelShouldLoadBreedFromIdBaseOnSavedState() = coroutineTestRule.runBlockingTest {
        val expectedBreed = Breed(id = "1", name = "name", group = "", origin = "", temperament = "", imageUrl = "")
        val savedStateHandle = SavedStateHandle().apply {
            set("breedId", "1")
        }
        val breedStoreMock = mockk<BreedStore>()
        every { breedStoreMock.getBreed("1") } returns flowOf(expectedBreed)
        val viewModel = BreedDetailViewModelDefault(savedStateHandle, breedStoreMock)
        val value = viewModel.breed.first()
        assertThat(value.name).matches(expectedBreed.name)
        assertThat(value.id).matches(expectedBreed.id)
    }
}