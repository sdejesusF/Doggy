package com.example.doggy.ui.breeddetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doggy.data.BreedStore
import com.example.doggy.data.emptyBreed
import com.example.doggy.domain.Breed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

interface BreedDetailViewModel {
    val breed: StateFlow<Breed>
}

@HiltViewModel
class BreedDetailViewModelDefault @Inject constructor(
    savedStateHandle: SavedStateHandle,
    breedStore: BreedStore,
): ViewModel(), BreedDetailViewModel {
    private val breedId: MutableStateFlow<String?> = MutableStateFlow(savedStateHandle.get<String>("breedId"))
    override val breed: StateFlow<Breed> = breedId.transformLatest { id ->
        id?.let {
            emitAll(breedStore.getBreed(id))
        } ?: emit(emptyBreed)
        }.stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(1000L), emptyBreed)
}