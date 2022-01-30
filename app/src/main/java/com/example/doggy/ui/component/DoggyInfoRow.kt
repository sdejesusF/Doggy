package com.example.doggy.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.doggy.R

@Composable
fun DoggyInfoRow(
    title: String,
    info: String?,
    modifier: Modifier = Modifier
) {
    Column {
        Row(modifier = modifier) {
            Text(text = title, modifier = Modifier.weight(4f))
            Text(text = valueOrUnknown(info), modifier = Modifier.weight(6f))
        }
        DoggyDefaultDivider()
    }
}

@Composable
private fun valueOrUnknown(value: String?): String {
    return if (value.isNullOrEmpty()) {
        stringResource(R.string.unknown)
    } else {
        value
    }
}
