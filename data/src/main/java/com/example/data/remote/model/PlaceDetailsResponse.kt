package com.example.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceDetailsResponse(
    @SerialName(value = "fsq_id")
    val id: String,
    @SerialName(value = "name")
    val name: String,
    @SerialName(value = "categories")
    val categories: List<CategoryResponse>?,
    @SerialName(value = "closed_bucket")
    val closedBucket: String?,
    @SerialName(value = "link")
    val link: String?,
    @SerialName(value = "timezone")
    val timeZone: String?,
)