package com.example.data.model

import com.example.domain.entity.result.PlaceSearchError

sealed class PlaceSearchLocalResult {
    object PlaceSearchLoading : PlaceSearchLocalResult()
    data class PlaceSearchSuccess(val places: List<PlaceLocal>, val nextPageCursor: String?) :
        PlaceSearchLocalResult()

    data class PlaceSearchFailed(
        val error: PlaceSearchError,
    ) : PlaceSearchLocalResult()
}