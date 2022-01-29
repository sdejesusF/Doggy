package com.example.doggy.ui.breeds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doggy.data.BreedStore
import com.example.doggy.data.sync.SyncOrchestrator
import com.example.doggy.data.sync.SyncStatus
import com.example.doggy.domain.Breed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

interface BreedsViewModel {
    val breeds: StateFlow<List<Breed>>
    val isGridView: StateFlow<Boolean>
    val syncStatus: StateFlow<SyncStatus>
    fun onRetry()
    fun onViewTypeSelected(isGridView: Boolean)
}

@HiltViewModel
class BreedsViewModelDefault @Inject constructor(
    private val breedStore: BreedStore,
    private val syncOrchestrator: SyncOrchestrator,
): ViewModel(), BreedsViewModel {

    override val breeds: MutableStateFlow<List<Breed>> = MutableStateFlow(listOf())
    override val isGridView: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val syncStatus: MutableStateFlow<SyncStatus> = MutableStateFlow(SyncStatus.COMPLETED)

    init {
        viewModelScope.launch {
            breedStore.getBreeds(0, 1000)
                .collect { r ->
                    delay(50)
                    breeds.value = r
                }
        }
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

    override fun onRetry() {
        syncOrchestrator.sync()
    }
}