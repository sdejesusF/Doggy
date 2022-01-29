package com.example.doggy.ui.component

import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DoggyDefaultDivider(
    modifier: Modifier = Modifier.height(1.dp)
) {
    Divider(modifier = modifier)
}
