package com.example.doggy.ui.breeddetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doggy.data.BreedStore
import com.example.doggy.data.emptyBreed
import com.example.doggy.domain.Breed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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
    override val breed: MutableStateFlow<Breed> = MutableStateFlow(emptyBreed)

    init {
        viewModelScope.launch {
            breedId.transformLatest { id ->
                id?.let {
                    emitAll(breedStore.getBreed(id ?: ""))
                } ?: emit(emptyBreed)
            }.shareIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), 1)
            .collect {
                delay(50)
                breed.value = it
            }
        }
    }
}