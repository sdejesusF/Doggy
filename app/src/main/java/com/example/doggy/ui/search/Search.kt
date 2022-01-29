package com.example.doggy.ui.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.doggy.R
import com.example.doggy.domain.Breed
import com.example.doggy.ui.component.NoContent
import com.example.doggy.ui.component.SearchResultItem
import com.example.doggy.ui.theme.DoggyTheme
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.TopAppBar

@Composable
fun Search(
    viewModel: SearchViewModel,
    onBreedDetail: (String) -> Unit,
) {
    val searchTerm = viewModel.searchTerm.collectAsState()
    val searchResult = viewModel.searchResult.collectAsState()

    SearchContent(
        searchTerm = searchTerm.value,
        searchResult = searchResult.value,
        onBreedDetail = onBreedDetail,
        onSearchTermChanged = viewModel::onSearchTermChanged,
    )
}

@Composable
fun SearchContent(
    searchTerm: String,
    searchResult: List<Breed>,
    onBreedDetail: (String) -> Unit,
    onSearchTermChanged: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            DoggySearchBreedsTopAppBar()
        }
    ) {
        LazyColumn {
            item {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    value = searchTerm,
                    placeholder = { Text(text = stringResource(R.string.start_typing)) },
                    onValueChange = { onSearchTermChanged(it) },
                    label = { Text(text = stringResource(id = R.string.search)) }
                )
            }

            if (searchTerm.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.type_message),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else if (searchResult.isEmpty()) {
                item {
                    NoContent(modifier = Modifier.fillMaxSize())
                }
            }

            items(searchResult) { breed ->
                SearchResultItem(
                    breed = breed,
                    onBreedDetail = onBreedDetail,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun DoggySearchBreedsTopAppBar() {
    TopAppBar(
        title = {},
        actions = {},
        contentPadding = rememberInsetsPaddingValues(
            LocalWindowInsets.current.statusBars,
            applyBottom = false,
        ),
    )
}

@Preview(showSystemUi = true)
@Composable
private fun SearchPreview() {
    DoggyTheme {
        SearchContent(
            searchTerm = "Typing",
            searchResult = listOf(),
            onSearchTermChanged = {},
            onBreedDetail = {},
        )
    }
}
