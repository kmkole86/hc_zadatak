package com.example.domain.entity

data class PlaceDetails(
    val id: String,
    val name: String,
    val isFavourite: Boolean,
    val iconUrl: String?,

    val closedBucket: String?,
    val link: String?,
    val timeZone: String?,
)