package com.example.domain.entity.result

import com.example.domain.entity.PlacePage

//TODO make to ListResult using <T, E>
sealed class PlaceSearchResult {
    object PlaceSearchLoading : PlaceSearchResult()
    data class PlaceSearchSuccess(val page: PlacePage) :
        PlaceSearchResult()

    data class PlaceSearchFailed(val error: PlaceSearchError) : PlaceSearchResult()
}

sealed class PlaceSearchError {
    object GenericError : PlaceSearchError()
    data class ApiError(val message: String? = null) : PlaceSearchError()
}