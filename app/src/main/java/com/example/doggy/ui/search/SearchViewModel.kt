package com.example.doggy.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doggy.data.BreedStore
import com.example.doggy.domain.Breed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

interface SearchViewModel {
    val searchTerm: StateFlow<String>
    val searchResult: StateFlow<List<Breed>>
    fun onSearchTermChanged(usertype: String)
}

@HiltViewModel
class SearchViewModelDefault @Inject constructor(
    private val breedStore: BreedStore
): ViewModel(), SearchViewModel {

    override val searchTerm: MutableStateFlow<String> = MutableStateFlow("")
    override val searchResult: MutableStateFlow<List<Breed>> = MutableStateFlow(listOf())

    init {
        viewModelScope.launch {
            delay(50)
            searchTerm
                .debounce(300)
                .flatMapLatest {
                    if (it.isEmpty()) {
                        flowOf(listOf())
                    } else {
                        breedStore.searchBreeds(it)
                    }
                }
                .collect {
                    searchResult.value = it
                }
        }
    }

    override fun onSearchTermChanged(usertype: String) {
        searchTerm.value = usertype
    }
}