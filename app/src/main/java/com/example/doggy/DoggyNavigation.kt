package com.example.doggy

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import com.example.doggy.ui.breeddetail.BreedDetail
import com.example.doggy.ui.breeddetail.BreedDetailViewModelDefault
import com.example.doggy.ui.breeds.Breeds
import com.example.doggy.ui.breeds.BreedsViewModelDefault
import com.example.doggy.ui.search.Search
import com.example.doggy.ui.search.SearchViewModelDefault
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

internal sealed class TopLevelNavigation(val route: String) {
    object Breeds : TopLevelNavigation("breeds")
    object Search : TopLevelNavigation("search")
}

private sealed class DestinationNavigation(
    private val route: String,
) {
    fun createRoute(root: TopLevelNavigation) = "${root.route}/$route"

    object Breeds : DestinationNavigation("breeds")
    object Search : DestinationNavigation("search")
    object BreedDetail : DestinationNavigation("breed/{breedId}") {
        fun createRoute(root: TopLevelNavigation, breedId: String): String {
            return "${root.route}/breed/$breedId"
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun DoggyNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = TopLevelNavigation.Breeds.route,
        modifier = modifier
    ) {
        addBreedsTopLevelNavigation(navController = navController)
        addSearchTopLevelNavigation(navController = navController)
    }
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
private fun NavGraphBuilder.addBreedsTopLevelNavigation(navController: NavController) {
    navigation(
        route = TopLevelNavigation.Breeds.route,
        startDestination = DestinationNavigation.Breeds.createRoute(TopLevelNavigation.Breeds)
    ) {
        addBreedsDestination(navController = navController, root = TopLevelNavigation.Breeds)
        addBreedDetailDestination(navController = navController, root = TopLevelNavigation.Breeds)
        addSearchDestination(navController = navController, root = TopLevelNavigation.Breeds)
    }
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
private fun NavGraphBuilder.addSearchTopLevelNavigation(navController: NavController) {
    navigation(
        route = TopLevelNavigation.Search.route,
        startDestination = DestinationNavigation.Search.createRoute(TopLevelNavigation.Search)
    ) {
        addBreedsDestination(navController = navController, root = TopLevelNavigation.Search)
        addBreedDetailDestination(navController = navController, root = TopLevelNavigation.Search)
        addSearchDestination(navController = navController, root = TopLevelNavigation.Search)
    }
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
private fun NavGraphBuilder.addBreedsDestination(
    navController: NavController,
    root: TopLevelNavigation
) {
    composable(route = DestinationNavigation.Breeds.createRoute(root)) {
        Breeds(
            onBreedDetail = { breedId ->
                navController.navigate(DestinationNavigation.BreedDetail.createRoute(root, breedId))
            },
            viewModel = hiltViewModel<BreedsViewModelDefault>()
        )
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addBreedDetailDestination(
    navController: NavController,
    root: TopLevelNavigation
) {
    composable(
        route = DestinationNavigation.BreedDetail.createRoute(root),
        arguments = listOf(
            navArgument("breedId") { type = NavType.StringType }
        )
    ) {
        BreedDetail(
            viewModel = hiltViewModel<BreedDetailViewModelDefault>(),
            navigateUp = navController::navigateUp
        )
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addSearchDestination(
    navController: NavController,
    root: TopLevelNavigation
) {
    composable(
        route = DestinationNavigation.Search.createRoute(root),
    ) {
        Search(
            onBreedDetail = { breedId ->
                navController.navigate(DestinationNavigation.BreedDetail.createRoute(root, breedId))
            },
            viewModel = hiltViewModel<SearchViewModelDefault>()
        )
    }
}