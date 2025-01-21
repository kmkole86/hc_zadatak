package com.example.a4square.features.home.places_search

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a4square.features.home.DetailsRoute
import com.example.a4square.features.home.SearchRoute
import com.example.a4square.features.home.navigateToPlaceDetails
import com.example.a4square.features.home.place_details.PlaceDetailsScreen

@Composable
fun PlaceSearchGraphScreen(modifier: Modifier= Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController,
        startDestination = SearchRoute,
    ) {
        composable<SearchRoute> {
            PlaceSearchScreen(onPlaceClicked = { placeId ->
                navController.navigateToPlaceDetails(
                    placeId = placeId
                )
            })
        }
        composable<DetailsRoute> {
            PlaceDetailsScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}