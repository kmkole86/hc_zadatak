package com.example.a4square.features.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.a4square.features.home.favourite.PlaceFavouritesGraphScreen
import com.example.a4square.features.home.places_search.PlaceSearchGraphScreen
import kotlinx.serialization.Serializable

@Serializable
object SearchGraphRoute

@Serializable
object FavouriteGraphRoute

@Serializable
object SearchRoute

@Serializable
data class DetailsRoute(val placeId: String)

@Serializable
object FavouriteRoute

fun NavController.navigateToPlaceDetails(placeId: String) {
    navigate(route = DetailsRoute(placeId = placeId))
}

fun NavGraphBuilder.searchGraph() {
    composable<SearchGraphRoute> {
        PlaceSearchGraphScreen()
    }
}

fun NavGraphBuilder.favouriteGraph() {
    composable<FavouriteGraphRoute> {
        PlaceFavouritesGraphScreen()
    }
}