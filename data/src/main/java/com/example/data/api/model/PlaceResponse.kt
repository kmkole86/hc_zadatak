package com.example.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceResponse(
    @SerialName(value = "fsq_id")
    val id: String,
    @SerialName(value = "name")
    val name: String,
    @SerialName(value = "distance")
    val distance: Int?,
    @SerialName(value = "categories")
    val categories: List<CategoryResponse>?
)