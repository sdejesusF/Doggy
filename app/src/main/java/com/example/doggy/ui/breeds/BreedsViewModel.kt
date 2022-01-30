package com.example.doggy.ui.breeds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.doggy.data.BreedStore
import com.example.doggy.data.sync.SyncOrchestrator
import com.example.doggy.data.sync.SyncStatus
import com.example.doggy.domain.Breed
import com.example.doggy.domain.Sort
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

interface BreedsViewModel {
    val isGridView: StateFlow<Boolean>
    val syncStatus: StateFlow<SyncStatus>
    val sorting: StateFlow<Sort>
    val breeds: Flow<PagingData<Breed>>
    fun onRetry()
    fun onViewTypeSelected(isGridView: Boolean)
    fun onSortingSelected(sorting: Sort)
}

@HiltViewModel
class BreedsViewModelDefault @Inject constructor(
    breedStore: BreedStore,
    private val syncOrchestrator: SyncOrchestrator,
): ViewModel(), BreedsViewModel {

    override val isGridView: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val syncStatus: MutableStateFlow<SyncStatus> = MutableStateFlow(SyncStatus.COMPLETED)
    override val sorting: MutableStateFlow<Sort> = MutableStateFlow(Sort.ASC)
    override val breeds: Flow<PagingData<Breed>> = sorting.transformLatest {
        s -> emitAll(breedStore.getBreedsPage(viewModelScope, s))
    }

    init {
        viewModelScope.launch {
            syncOrchestrator.syncStatus.collect {
                delay(50)
                syncStatus.value = it
            }
        }
    }

    override fun onViewTypeSelected(isGridView: Boolean) {
        this.isGridView.value = isGridView
    }

    override fun onSortingSelected(sorting: Sort) {
        this.sorting.value = sorting
    }

    override fun onRetry() {
        syncOrchestrator.sync()
    }
}