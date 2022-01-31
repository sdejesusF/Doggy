package com.example.doggy.ui.breeds

import androidx.paging.PagingData
import com.example.doggy.MainCoroutineRule
import com.example.doggy.data.BreedStore
import com.example.doggy.data.sync.SyncOrchestrator
import com.example.doggy.data.sync.SyncStatus
import com.example.doggy.domain.Breed
import com.example.doggy.domain.Sort
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

class BreedsViewModelDefaultTest {

    private val breed1 = Breed(id = "1", name = "A", group = "", origin = "", temperament = "", imageUrl = "")
    private val breed2 = Breed(id = "2", name = "B", group = "", origin = "", temperament = "", imageUrl = "")
    private val pagingDataAsc = PagingData.from(listOf(breed1, breed2))
    private val pagingDataDesc = PagingData.from(listOf(breed1, breed2))

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = MainCoroutineRule()

    @Test
    fun viewModelShouldBeLoadingWhenSyncIsRunning() = coroutineTestRule.runBlockingTest {
        val mockBreedStore = mockk<BreedStore>()
        val mockSyncOrchestrator = mockk<SyncOrchestrator>()
        every { mockSyncOrchestrator.syncStatus } returns flowOf(SyncStatus.RUNNING)
        val viewModel = BreedsViewModelDefault(mockBreedStore, mockSyncOrchestrator)
        val value = viewModel.syncStatus.first()
        assertThat(value).isEqualTo(SyncStatus.RUNNING)
    }

    @Test
    fun viewModelShouldChangeViewTypeWhenOnViewTypeSelectedIsCalled() = coroutineTestRule.runBlockingTest {
        val mockBreedStore = mockk<BreedStore>()
        val mockSyncOrchestrator = mockk<SyncOrchestrator>()
        val viewModel = BreedsViewModelDefault(mockBreedStore, mockSyncOrchestrator)
        assertThat(viewModel.isGridView.value).isEqualTo(false)
        viewModel.onViewTypeSelected(true)
        assertThat(viewModel.isGridView.value).isEqualTo(true)
   }

    @Test
    fun viewModelShouldChangeSortingWhenOnSortSelectedIsCalled() = coroutineTestRule.runBlockingTest {
        val mockBreedStore = mockk<BreedStore>()
        val mockSyncOrchestrator = mockk<SyncOrchestrator>()
        val viewModel = BreedsViewModelDefault(mockBreedStore, mockSyncOrchestrator)
        assertThat(viewModel.sorting.value).isEqualTo(Sort.ASC)
        viewModel.onSortingSelected(Sort.DESC)
        assertThat(viewModel.sorting.value).isEqualTo(Sort.DESC)
    }

    @Test
    fun viewModelShouldLoadListBasedOnSorting() = coroutineTestRule.runBlockingTest {
        val mockBreedStore = mockk<BreedStore>()
        val mockSyncOrchestrator = mockk<SyncOrchestrator>()
        every { mockSyncOrchestrator.syncStatus } returns flowOf(SyncStatus.RUNNING)
        every { mockBreedStore.getBreedsPage(any(), Sort.ASC) } returns flowOf(pagingDataAsc)
        every { mockBreedStore.getBreedsPage(any(), Sort.DESC) } returns flowOf(pagingDataDesc)
        val viewModel = BreedsViewModelDefault(mockBreedStore, mockSyncOrchestrator)
        viewModel.breeds.first()
        verify { mockBreedStore.getBreedsPage(any(), Sort.ASC)  }
        viewModel.onSortingSelected(Sort.DESC)
        viewModel.breeds.first()
        verify { mockBreedStore.getBreedsPage(any(), Sort.DESC)  }
    }
}