package com.example.doggy.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.doggy.R
import com.example.doggy.data.emptyBreed
import com.example.doggy.domain.Breed
import com.example.doggy.ui.theme.DoggyTheme

@Composable
fun DoggyImage(
    breed: Breed,
    modifier: Modifier = Modifier,
) {
    val painter = breed.imageUrl?.let { url ->
        rememberImagePainter(url) }
        ?: painterResource(R.drawable.ic_dog)
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .width(150.dp)
                .height(100.dp)
        )
    }
}

@Preview
@Composable
private fun DoggyImagePreview() {
    DoggyTheme {
        DoggyImage(emptyBreed)
    }
}