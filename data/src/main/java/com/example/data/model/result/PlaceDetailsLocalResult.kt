package com.example.data.model.result

import com.example.data.model.PlaceDetailsLocal
import com.example.domain.entity.result.PlaceDetailsError

sealed class PlaceDetailsLocalResult {
    object PlaceDetailsLoading : PlaceDetailsLocalResult()
    data class PlaceDetailsSuccess(val placeDetails: PlaceDetailsLocal?) :
        PlaceDetailsLocalResult()

    data class PlaceDetailsFailed(
        val error: PlaceDetailsError,
    ) : PlaceDetailsLocalResult()
}

//dva pristupa za favourite da se slusaju odvojeno pa da se merguju u UI-u ili sve u data layeru