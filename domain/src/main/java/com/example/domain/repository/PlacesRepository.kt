package com.example.domain.repository

import com.example.domain.entity.result.FavouriteStatusResult
import com.example.domain.entity.result.PlaceResult
import com.example.domain.entity.result.PlaceSearchResult
import kotlinx.coroutines.flow.Flow

interface PlacesRepository {

    fun getPlaceDetails(placeId: String): Flow<PlaceResult>

    fun searchPlace(query: String, pageCursor: String? = null): Flow<PlaceSearchResult>

    fun changePlaceFavouriteStatus(placeId: String): Flow<FavouriteStatusResult>
}