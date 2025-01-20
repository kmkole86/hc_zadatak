package com.example.domain.entity.result

import com.example.domain.entity.Place

//TODO make to ListResult using <T, E>
sealed class PlaceSearchResult {

    abstract val places: List<Place>
    abstract val nextPageCursor: String?

    data class PlaceSearchLoading(
        override val places: List<Place>,
        override val nextPageCursor: String?
    ) : PlaceSearchResult()

    data class PlaceSearchSuccess(
        override val places: List<Place>,
        override val nextPageCursor: String?
    ) :
        PlaceSearchResult() {
        companion object {
            fun empty(): PlaceSearchSuccess =
                PlaceSearchSuccess(places = listOf(), nextPageCursor = null)
        }
    }

    data class PlaceSearchFailed(
        override val places: List<Place>,
        override val nextPageCursor: String?,
        val error: PlaceSearchError,
    ) : PlaceSearchResult()
}

sealed class PlaceSearchError {
    object GenericError : PlaceSearchError()
    data class ApiError(val message: String? = null) : PlaceSearchError()
}