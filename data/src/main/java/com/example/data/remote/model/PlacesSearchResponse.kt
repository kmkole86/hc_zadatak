package com.example.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlacesSearchResponse(
    @SerialName(value = "results")
    val results: List<PlaceResponse>
)

