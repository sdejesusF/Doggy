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
import kotlinx.coroutines.flow.*
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
    override val syncStatus: StateFlow<SyncStatus> = flow {
        syncOrchestrator.syncStatus.collect {
            emit(it)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), initialValue = SyncStatus.COMPLETED)

    override val sorting: MutableStateFlow<Sort> = MutableStateFlow(Sort.ASC)
    override val breeds: Flow<PagingData<Breed>> = sorting.transformLatest {
        s -> emitAll(breedStore.getBreedsPage(viewModelScope, s))
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