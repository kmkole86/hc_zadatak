package com.example.domain.entity

data class PlacePage(val places: List<Place>, val nextPageCursor: String?) {
    companion object {
        fun empty() = PlacePage(places = emptyList(), nextPageCursor = null)
    }
}