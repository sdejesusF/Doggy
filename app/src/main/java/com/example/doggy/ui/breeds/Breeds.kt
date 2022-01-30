package com.example.doggy.ui.breeds

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Grid4x4
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.SortByAlpha
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
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.doggy.R
import com.example.doggy.data.sync.SyncStatus
import com.example.doggy.domain.Breed
import com.example.doggy.domain.Sort
import com.example.doggy.ui.component.BreedItem
import com.example.doggy.ui.component.NoContent
import com.example.doggy.ui.theme.DoggyTheme
import com.example.doggy.ui.util.itemsInGrid
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.TopAppBar
import kotlinx.coroutines.flow.flowOf

@Composable
@ExperimentalFoundationApi
fun Breeds(viewModel: BreedsViewModel,
           onBreedDetail: (String) -> Unit
) {
    val isGridView = viewModel.isGridView.collectAsState()
    val syncStatus = viewModel.syncStatus.collectAsState()
    val breedsPagingItems = viewModel.breeds.collectAsLazyPagingItems()
    val sorting = viewModel.sorting.collectAsState()

    BreedsContent(
        isGridView = isGridView.value,
        syncStatus = syncStatus.value,
        sorting = sorting.value,
        breedsPagingListItems = breedsPagingItems,
        onSortingSelected = viewModel::onSortingSelected,
        onViewTypeSelected = viewModel::onViewTypeSelected,
        onRetry = viewModel::onRetry,
        onBreedDetail = onBreedDetail,
    )
}

@Composable
@ExperimentalFoundationApi
fun BreedsContent(
    isGridView: Boolean,
    syncStatus: SyncStatus,
    sorting: Sort,
    breedsPagingListItems: LazyPagingItems<Breed>,
    onSortingSelected: (sorting: Sort) -> Unit,
    onRetry: () -> Unit,
    onViewTypeSelected: (isGridView: Boolean) -> Unit,
    onBreedDetail: (String) -> Unit
) {
    Scaffold(
        topBar = {
            DoggyBreedsTopAppBar(
                isGridView = isGridView,
                onViewTypeSelected = onViewTypeSelected,
                sorting = sorting,
                onSortingSelected = onSortingSelected,
            )
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
                        isGridView = isGridView,
                        loadingHeader = { SyncStatus(syncStatus = syncStatus, onRetry = onRetry) },
                        breedsPagingListItems = breedsPagingListItems,
                        onBreedDetail = onBreedDetail,
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
    sorting: Sort,
    onViewTypeSelected: (isGridView: Boolean) -> Unit,
    onSortingSelected: (sorting: Sort) -> Unit,
) {
    val painterListMode = if (isGridView)
        rememberVectorPainter(Icons.Default.List) else
        rememberVectorPainter(Icons.Default.Grid4x4)
    val painterSortingMode = rememberVectorPainter(Icons.Default.SortByAlpha)
    TopAppBar(
        title = { },
        actions = {
            Icon(
                painter = painterSortingMode,
                contentDescription = null,
                modifier = Modifier.clickable {
                    onSortingSelected(when (sorting) {
                        Sort.ASC -> Sort.DESC
                        Sort.DESC -> Sort.ASC
                    })
                }
            )
            Spacer(modifier = Modifier.size(8.dp))
            Icon(
                painter = painterListMode,
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
    isGridView: Boolean,
    breedsPagingListItems: LazyPagingItems<Breed>,
    loadingHeader: @Composable () -> Unit,
    onBreedDetail: (id: String) -> Unit
) = if (isGridView)
        GridView(
            loadingHeader = loadingHeader,
            modifier = modifier,
            breedsPagingListItems = breedsPagingListItems,
            onBreedDetail = onBreedDetail,
        )
    else ListView(
            loadingHeader = loadingHeader,
            modifier = modifier,
            breedsPagingListItems = breedsPagingListItems,
            onBreedDetail = onBreedDetail,
        )

@ExperimentalFoundationApi
@Composable
private fun GridView(
    modifier: Modifier = Modifier,
    breedsPagingListItems: LazyPagingItems<Breed>,
    loadingHeader: @Composable () -> Unit,
    onBreedDetail: (id: String) -> Unit
) {

    LazyColumn {
        item {
            loadingHeader()
        }
        itemsInGrid(
            lazyPagingItems = breedsPagingListItems, columns = 2) { breed ->
            breed?.let {
                BreedItem(breed = it, onBreedDetail = onBreedDetail)
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
private fun ListView(
    modifier: Modifier = Modifier,
    breedsPagingListItems: LazyPagingItems<Breed>,
    loadingHeader: @Composable () -> Unit,
    onBreedDetail: (id: String) -> Unit
) {

    LazyColumn {
        stickyHeader {
            loadingHeader()
        }
        items(breedsPagingListItems) { breed ->
            breed?.let {
                BreedItem(breed = breed, onBreedDetail = onBreedDetail)
            }
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
            isGridView =  false,
            syncStatus = SyncStatus.FAILED,
            sorting = Sort.ASC,
            onSortingSelected = {},
            onRetry = {},
            breedsPagingListItems = flowOf(PagingData.from(breeds)).collectAsLazyPagingItems(),
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
            isGridView =  true,
            syncStatus = SyncStatus.RUNNING,
            breedsPagingListItems = flowOf(PagingData.from(breeds)).collectAsLazyPagingItems(),
            sorting = Sort.ASC,
            onSortingSelected = {},
            onRetry = {},
            onBreedDetail = {},
            onViewTypeSelected = {}
        )
    }
}