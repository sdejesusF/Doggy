package com.example.doggy.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.doggy.R
import com.example.doggy.domain.Breed
import com.example.doggy.ui.theme.DoggyTheme

@Composable
fun SearchResultItem(
    modifier: Modifier = Modifier,
    breed: Breed,
    onBreedDetail: (id: String) -> Unit
) {
    Column(
        modifier = modifier
            .clickable { onBreedDetail(breed.id) }
    ) {
        DoggyDefaultDivider()
        DoggyInfoRow(title = stringResource(id = R.string.name_with_desc), info = breed.name)
        DoggyInfoRow(title = stringResource(id = R.string.group_with_desc), info = breed.group)
        DoggyInfoRow(title = stringResource(id = R.string.origin_with_desc), info = breed.origin)
    }
}

@Composable
@Preview
fun SearchResultItemPreview() {
    DoggyTheme {
        val breed = Breed(id = "1", name = "Cute", imageUrl = null, group = null, origin = null, temperament = null)
        SearchResultItem(breed = breed, onBreedDetail = {})
    }
}