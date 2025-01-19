package com.example.a4square.features.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

data class TopLevelRoute<T : Any>(val name: String, val route: T, val icon: ImageVector)

val topLevelRoutes = listOf(
    TopLevelRoute<SearchGraphRoute>(
        name = "Places",
        route = SearchGraphRoute,
        icon = Icons.Outlined.Search
    ),
    TopLevelRoute<FavouriteGraphRoute>(
        name = "Favourites",
        route = FavouriteGraphRoute,
        icon = Icons.Outlined.FavoriteBorder
    )
)