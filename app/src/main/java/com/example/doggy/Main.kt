package com.example.doggy

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.doggy.ui.theme.DoggyTheme
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.BottomNavigation
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun Main() {
    DoggyTheme {
        ProvideWindowInsets {
            val navController = rememberAnimatedNavController()
            MainContent(navController)
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
private fun MainContent(navController: NavHostController) {
    Scaffold(
        bottomBar = {
            val currentSelectedItem by navController.currentScreenAsState()
            DoggyBottomNavigation(selectedNavigation = currentSelectedItem, onNavigationSelected = { selected ->
                navController.navigate(selected.route) {
                    launchSingleTop = true
                    restoreState = true

                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                }
            })
        }
    ) {
        Row(modifier = Modifier
            .padding(paddingValues = it)
            .fillMaxSize()) {
            DoggyNavigation(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                navController = navController,
            )
        }
    }
}

@Composable
internal fun DoggyBottomNavigation(
    modifier: Modifier = Modifier,
    selectedNavigation: TopLevelNavigation,
    onNavigationSelected: (TopLevelNavigation) -> Unit,
) {
    BottomNavigation(
        modifier = modifier,
        contentPadding = rememberInsetsPaddingValues(LocalWindowInsets.current.navigationBars),
    ) {
        BottomNavigationItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_dog),
                    contentDescription = null,
                )
            },
            label = {
                Text(text = stringResource(R.string.breeds))
            },
            selected = selectedNavigation == TopLevelNavigation.Breeds,
            onClick = { onNavigationSelected(TopLevelNavigation.Breeds) }
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    painter = rememberVectorPainter(Icons.Default.Search),
                    contentDescription = null,
                )
            },
            label = {
                Text(text = stringResource(R.string.search))
            },
            selected = selectedNavigation == TopLevelNavigation.Search,
            onClick = { onNavigationSelected(TopLevelNavigation.Search) }
        )
    }
}

@Stable
@Composable
private fun NavController.currentScreenAsState(): State<TopLevelNavigation> {
    val selectedItem = remember { mutableStateOf<TopLevelNavigation>(TopLevelNavigation.Breeds) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.hierarchy.any { it.route == TopLevelNavigation.Breeds.route } -> {
                    selectedItem.value = TopLevelNavigation.Breeds
                }
                destination.hierarchy.any { it.route == TopLevelNavigation.Search.route } -> {
                    selectedItem.value = TopLevelNavigation.Search
                }
            }
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return selectedItem
}