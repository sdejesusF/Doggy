package com.example.doggy.ui.breeddetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.doggy.R
import com.example.doggy.data.emptyBreed
import com.example.doggy.domain.Breed
import com.example.doggy.ui.component.DoggyDefaultDivider
import com.example.doggy.ui.component.DoggyImage
import com.example.doggy.ui.component.DoggyInfoRow
import com.example.doggy.ui.theme.DoggyTheme
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.TopAppBar

@Composable
fun BreedDetail(
    viewModel: BreedDetailViewModel,
    navigateUp: () -> Unit) {
    val breed = viewModel.breed.collectAsState()

    BreedDetailContent(breed = breed.value, navigateUp = navigateUp)
}

@Composable
fun BreedDetailContent(
    breed: Breed,
    navigateUp: () -> Unit
) {
    Scaffold(
        topBar = {
            DoggyBreedDetailTopAppBar(navigateUp = navigateUp)
        }
    ) {
        LazyColumn(modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
        ) {
            item {
                DoggyImage(breed = breed)
            }
            item {
                DoggyDefaultDivider(modifier = Modifier.height(1.dp).padding(8.dp))
            }
            item {
                DoggyInfoRow(title = stringResource(R.string.name_with_desc), info = breed.name, modifier = Modifier.padding(8.dp))
                DoggyInfoRow(title = stringResource(R.string.category_with_desc), info = stringResource(R.string.unknown), modifier = Modifier.padding(8.dp))
                DoggyInfoRow(title = stringResource(R.string.origin_with_desc), info = breed.origin, modifier = Modifier.padding(8.dp))
                DoggyInfoRow(title = stringResource(R.string.temp_with_desc), info = breed.temperament, modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
private fun DoggyBreedDetailTopAppBar(navigateUp: () -> Unit) {
    TopAppBar(
        title = { },
        actions = {
        },
        contentPadding = rememberInsetsPaddingValues(
            LocalWindowInsets.current.statusBars,
            applyBottom = false,
        ),
        navigationIcon = {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "",
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        navigateUp()
                    }
            )
        }
    )
}

@Preview
@Composable
private fun BreedDetailPreview() {
    DoggyTheme {
        BreedDetailContent(breed = emptyBreed, navigateUp = {})
    }
}