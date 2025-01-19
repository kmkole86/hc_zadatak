package com.example.a4square.features.home.place_details

import com.example.domain.entity.result.FavouriteStatusResult
import com.example.domain.entity.result.PlaceResult

data class PlaceDetailsState(
    val place: PlaceResult? = null,
    val favouriteStatus: FavouriteStatusResult? = null
) {
    companion object {
        fun empty() = PlaceDetailsState()
    }
}