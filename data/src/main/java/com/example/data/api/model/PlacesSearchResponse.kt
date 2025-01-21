package com.example.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlacesSearchResponse(
    @SerialName(value = "results")
    val results: List<PlaceResponse>
)

