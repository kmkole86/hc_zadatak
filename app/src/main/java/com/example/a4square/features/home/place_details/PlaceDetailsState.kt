package com.example.a4square.features.home.place_details

import androidx.compose.runtime.Stable
import com.example.domain.entity.PlaceDetails
import com.example.domain.entity.result.PlaceDetailsError

@Stable
sealed class PlaceDetailsState {
    object PlaceDetailsLoading : PlaceDetailsState()
    data class PlaceDetailsSuccess(val placeDetails: PlaceDetails?) : PlaceDetailsState()
    data class PlaceDetailsFailed(val error: PlaceDetailsError) : PlaceDetailsState()
}