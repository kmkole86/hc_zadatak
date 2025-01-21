package com.example.domain.entity.result

import com.example.domain.entity.PlaceDetails

//TODO make to SingleResult using <T, E>
sealed class PlaceDetailsResult {
    object PlaceDetailsLoading : PlaceDetailsResult()
    data class PlaceDetailsSuccess(val placeDetails: PlaceDetails?) : PlaceDetailsResult()
    data class PlaceDetailsFailed(val error: PlaceDetailsError) : PlaceDetailsResult()
}

sealed class PlaceDetailsError {
    object GenericError : PlaceDetailsError()
    data class ApiError(val message: String? = null) : PlaceDetailsError()
}