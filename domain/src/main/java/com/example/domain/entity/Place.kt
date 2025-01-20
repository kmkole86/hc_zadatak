package com.example.domain.entity

data class Place(
    val id: String,
    val distance: Int,
    val name: String,
    val isFavourite: Boolean,
    val iconUrl: String?
)