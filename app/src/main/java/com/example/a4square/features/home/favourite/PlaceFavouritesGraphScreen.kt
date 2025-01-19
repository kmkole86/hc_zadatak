package com.example.a4square.features.home.favourite

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a4square.features.home.DetailsRoute
import com.example.a4square.features.home.FavouriteRoute
import com.example.a4square.features.home.navigateToPlaceDetails
import com.example.a4square.features.home.place_details.PlaceDetailsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceFavouritesGraphScreen() {
    val navController = rememberNavController()
    NavHost(
        navController,
        startDestination = FavouriteRoute,
    ) {
        composable<FavouriteRoute> {
            PlaceFavouritesScreen(onPlaceClicked = { placeId ->
                navController.navigateToPlaceDetails(
                    placeId = placeId
                )
            })
        }
        composable<DetailsRoute> {


            PlaceDetailsScreen() }
    }
}