package com.example.doggy.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.doggy.domain.Breed
import com.example.doggy.ui.theme.DoggyTheme

@Composable
fun BreedItem(
    modifier: Modifier = Modifier,
    breed: Breed,
    onBreedDetail: (id: String) -> Unit
) {
    Column(
        modifier = modifier
            .clickable { onBreedDetail(breed.id) }
    ) {
        DoggyImage(breed = breed)
        Text(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            text = breed.name,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
@Preview
fun BreedItemPreview() {
    DoggyTheme {
        val breed = Breed(id = "1", name = "Cute", imageUrl = null, group = null, origin = null, temperament = null)
        BreedItem(breed = breed, onBreedDetail = {})
    }
}