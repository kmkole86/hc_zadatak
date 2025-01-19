package com.example.data.repository_impl

import com.example.data.local.data_source_impl.PlacesLocalDataSource
import com.example.data.remote.data_source_impl.PlacesRemoteDataSource
import com.example.data.remote.model.PlaceResponse
import com.example.domain.entity.Place
import com.example.domain.entity.PlacePage
import com.example.domain.entity.result.FavouriteStatusResult
import com.example.domain.entity.result.PlaceResult
import com.example.domain.entity.result.PlaceSearchResult
import com.example.domain.repository.PlacesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart

class PlacesRepositoryImpl(
    private val dispatcher: CoroutineDispatcher,
    private val localDatasource: PlacesLocalDataSource,
    private val remoteDataSource: PlacesRemoteDataSource
) : PlacesRepository {
    override fun getPlaceDetails(placeId: String): Flow<PlaceResult> = flow<PlaceResult> {
        try {

        } catch (e: Exception) {

        }
    }.onStart { emit(PlaceResult.PlaceLoading) }.flowOn(dispatcher)

    override fun searchPlace(query: String, pageCursor: String?): Flow<PlaceSearchResult> =
        flow<PlaceSearchResult> {

            val result = remoteDataSource.searchPlace(query = query, pageCursor = pageCursor)

            //proveri za favourites
            result.onSuccess {
                emit(
                    PlaceSearchResult.PlaceSearchSuccess(
                        page = PlacePage(
                            places = it.places.map { place -> place.mapToPlace() },
                            nextPageCursor = it.nextPageCursor
                        )
                    )
                )
            }.onFailure { }
        }.onStart { emit(PlaceSearchResult.PlaceSearchLoading) }.flowOn(dispatcher)

    override fun changePlaceFavouriteStatus(placeId: String): Flow<FavouriteStatusResult> =
        flow<FavouriteStatusResult> {
            try {

            } catch (e: Exception) {

            }
        }.onStart { emit(FavouriteStatusResult.FavouriteStatusLoading) }.flowOn(dispatcher)
}

fun PlaceResponse.mapToPlace() =
    Place(id = id, distance = distance, name = name, isFavourite = false)