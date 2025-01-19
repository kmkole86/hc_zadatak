package com.example.domain.entity.result

import com.example.domain.entity.Place

//TODO make to SingleResult using <T, E>
sealed class PlaceResult {
    object PlaceLoading : PlaceResult()
    data class PlaceSuccess(val place: Place) : PlaceResult()
    data class PlaceFailed(val error: GetPlaceByIdError) : PlaceResult()
}

sealed class GetPlaceByIdError {
    object GenericError : GetPlaceByIdError()
    data class ApiError(val message: String? = null) : GetPlaceByIdError()
}