package com.example.domain.repository

import com.example.domain.entity.result.FavouriteStatusResult
import com.example.domain.entity.result.PlaceDetailsResult
import com.example.domain.entity.result.PlaceSearchResult
import kotlinx.coroutines.flow.Flow

interface PlacesRepository {

    fun getPlaceDetails(placeId: String): Flow<PlaceDetailsResult>

    fun searchPlace(query: String, pageCursor: String? = null): Flow<PlaceSearchResult>

    fun changePlaceFavouriteStatus(placeId: String): Flow<FavouriteStatusResult>

    fun getFavouritePlaces(): Flow<PlaceSearchResult>
}