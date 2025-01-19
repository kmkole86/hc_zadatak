package com.example.a4square.features.home.place_details

import androidx.compose.runtime.Stable

@Stable
sealed class PlaceDetailsEvent {
    data class PlaceDetailsGetDataEvent(val placeId: String) : PlaceDetailsEvent()
    object ChangeFavouriteStatusEvent : PlaceDetailsEvent()
}