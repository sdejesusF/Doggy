package com.example.doggy.ui.search

import com.example.doggy.MainCoroutineRule
import com.example.doggy.data.BreedStore
import com.example.doggy.data.emptyBreed
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

class SearchViewModelDefaultTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = MainCoroutineRule()

    @Test
    fun viewModelShouldLoadDataAfterUserTypes() = coroutineTestRule.runBlockingTest {
        val mock = mockk<BreedStore>()
        every { mock.searchBreeds("A") } returns flowOf(listOf(emptyBreed))

        val viewModel = SearchViewModelDefault(mock)
        viewModel.onSearchTermChanged("A")
        delay(500)
        assertThat(viewModel.searchResult.value).isNotEmpty()
    }

    @Test
    fun viewModelShouldSetEmptyWhenValueIsEmpty() = coroutineTestRule.runBlockingTest {
        val mock = mockk<BreedStore>()
        every { mock.searchBreeds("") } returns flowOf(listOf(emptyBreed))

        val viewModel = SearchViewModelDefault(mock)
        viewModel.onSearchTermChanged("")
        delay(500)
        assertThat(viewModel.searchResult.value).isEmpty()
    }
}