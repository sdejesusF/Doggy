package com.example.doggy.ui.breeds

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Grid4x4
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.doggy.R
import com.example.doggy.data.sync.SyncStatus
import com.example.doggy.domain.Breed
import com.example.doggy.ui.component.BreedItem
import com.example.doggy.ui.component.NoContent
import com.example.doggy.ui.theme.DoggyTheme
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.TopAppBar

@Composable
@ExperimentalFoundationApi
fun Breeds(viewModel: BreedsViewModel,
           onBreedDetail: (String) -> Unit
) {
    val breeds = viewModel.breeds.collectAsState()
    val isGridView = viewModel.isGridView.collectAsState()
    val syncStatus = viewModel.syncStatus.collectAsState()

    BreedsContent(
        breeds = breeds.value,
        isGridView = isGridView.value,
        syncStatus = syncStatus.value,
        onViewTypeSelected = viewModel::onViewTypeSelected,
        onRetry = viewModel::onRetry,
        onBreedDetail = onBreedDetail
    )
}

@Composable
@ExperimentalFoundationApi
fun BreedsContent(
    breeds: List<Breed>,
    isGridView: Boolean,
    syncStatus: SyncStatus,
    onRetry: () -> Unit,
    onViewTypeSelected: (isGridView: Boolean) -> Unit,
    onBreedDetail: (String) -> Unit
) {
    Scaffold(
        topBar = {
            DoggyBreedsTopAppBar(isGridView = isGridView, onViewTypeSelected = onViewTypeSelected)
        }
    ) {
        Column(modifier = Modifier.padding(paddingValues = it)) {
            Box {
                if (breeds.isEmpty()) {
                    Column {
                        SyncStatus(syncStatus = syncStatus, onRetry = onRetry)
                        NoContent(modifier = Modifier.fillMaxSize())
                    }
                } else {
                    BreedsList(
                        breeds = breeds,
                        isGridView = isGridView,
                        loadingHeader = { SyncStatus(syncStatus = syncStatus, onRetry = onRetry) },
                        onBreedDetail = onBreedDetail
                    )
                }
            }
        }
    }
}

@Composable
fun SyncStatus(
    syncStatus: SyncStatus,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) =  when(syncStatus) {
    SyncStatus.RUNNING -> @Composable {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            CircularProgressIndicator(modifier = modifier.size(24.dp))
        }
    }
    SyncStatus.FAILED -> @Composable {
        Column(modifier = modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.error_syncing),
                textAlign = TextAlign.Center,
                modifier = modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.error),
                color = MaterialTheme.colors.onError
            )
            Button(onClick = onRetry, modifier = Modifier.align(CenterHorizontally)) {
                Text(text = stringResource(R.string.retry))
            }
        }
        
    }
    SyncStatus.COMPLETED -> @Composable { }
}


@Composable
private fun DoggyBreedsTopAppBar(
    isGridView: Boolean,
    onViewTypeSelected: (isGridView: Boolean) -> Unit
) {
    val painter = if (isGridView)
        rememberVectorPainter(Icons.Default.List) else
        rememberVectorPainter(Icons.Default.Grid4x4)
    TopAppBar(
        title = { },
        actions = {
            Icon(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.clickable {
                    onViewTypeSelected(!isGridView)
                }
            )
        },
        contentPadding = rememberInsetsPaddingValues(
            LocalWindowInsets.current.statusBars,
            applyBottom = false,
        ),
    )
}

@Composable
@ExperimentalFoundationApi
private fun BreedsList(
    modifier: Modifier = Modifier,
    breeds: List<Breed>,
    isGridView: Boolean,
    loadingHeader: @Composable () -> Unit,
    onBreedDetail: (id: String) -> Unit
) = if (isGridView) GridView(loadingHeader = loadingHeader, modifier = modifier, breeds = breeds, onBreedDetail = onBreedDetail)
    else ListView(loadingHeader = loadingHeader, modifier = modifier, breeds = breeds, onBreedDetail = onBreedDetail)

@ExperimentalFoundationApi
@Composable
private fun GridView(
    modifier: Modifier = Modifier,
    breeds: List<Breed>,
    loadingHeader: @Composable () -> Unit,
    onBreedDetail: (id: String) -> Unit
) {
    loadingHeader()
    LazyVerticalGrid(cells = GridCells.Fixed(2)) {
        items(breeds) { breed ->
            BreedItem(breed = breed, onBreedDetail = onBreedDetail)
        }
    }
}

@ExperimentalFoundationApi
@Composable
private fun ListView(
    modifier: Modifier = Modifier,
    breeds: List<Breed>,
    loadingHeader: @Composable () -> Unit,
    onBreedDetail: (id: String) -> Unit
) {
    LazyColumn {
        stickyHeader {
            loadingHeader()
        }
        items(items = breeds) { breed ->
            BreedItem(breed = breed, onBreedDetail = onBreedDetail)
        }
    }
}

val breeds = listOf(
    Breed(id = "1", name = "Doggy", temperament = "", origin = "", group = "", imageUrl = ""),
    Breed(id = "2", name = "Doggy2", temperament = "", origin = "", group = "", imageUrl = ""),
    Breed(id = "3", name = "Doggy3", temperament = "", origin = "", group = "", imageUrl = ""),
    Breed(id = "4", name = "Doggy4", temperament = "", origin = "", group = "", imageUrl = ""),
)

@Preview
@Composable
@ExperimentalFoundationApi
private fun BreedsPreviewList() {
    DoggyTheme {
        BreedsContent(
            breeds = breeds,
            isGridView =  false,
            syncStatus = SyncStatus.FAILED,
            onRetry = {},
            onBreedDetail = {},
            onViewTypeSelected = {}
        )
    }
}

@Preview
@Composable
@ExperimentalFoundationApi
private fun BreedsPreviewGrid() {
    DoggyTheme {
        BreedsContent(
            breeds = breeds,
            isGridView =  true,
            syncStatus = SyncStatus.RUNNING,
            onRetry = {},
            onBreedDetail = {},
            onViewTypeSelected = {}
        )
    }
}