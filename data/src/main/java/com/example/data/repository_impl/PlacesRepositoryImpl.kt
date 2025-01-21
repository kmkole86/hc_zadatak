package com.example.data.repository_impl

import com.example.data.api.data_source_impl.PlacesRemoteDataSource
import com.example.data.common.mapToDomain
import com.example.data.common.mapToLocal
import com.example.data.database.data_source_impl.PlacesLocalDataSource
import com.example.data.device.data_source_impl.DeviceNetworkDataSource
import com.example.data.model.PlaceDetailsLocalWithFavourite
import com.example.data.model.PlaceSearchLocalResult
import com.example.data.model.result.PlaceDetailsLocalResult
import com.example.data.model.result.PlaceLocalWithFavourite
import com.example.domain.entity.result.FavouriteStatusResult
import com.example.domain.entity.result.PlaceDetailsError
import com.example.domain.entity.result.PlaceDetailsResult
import com.example.domain.entity.result.PlaceSearchError
import com.example.domain.entity.result.PlaceSearchResult
import com.example.domain.repository.PlacesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class PlacesRepositoryImpl(
    private val dispatcher: CoroutineDispatcher,
    private val deviceNetworkDataSource: DeviceNetworkDataSource,
    private val localDatasource: PlacesLocalDataSource,
    private val remoteDataSource: PlacesRemoteDataSource
) : PlacesRepository {

    override fun searchPlace(query: String, pageCursor: String?): Flow<PlaceSearchResult> =
        combine<PlaceSearchLocalResult, List<PlaceLocalWithFavourite>, PlaceSearchResult>(
            flow = if (deviceNetworkDataSource.internetAvailable()) getPlacesFromApiAndCashItLocally(
                query = query,
                pageCursor = pageCursor
            ) else getPlacesFromCacheAndUpdateIndex(query = query, pageCursor = pageCursor),
            flow2 = localDatasource.observeSearchIndex()
        ) { apiResult, searchIndex ->
            when (apiResult) {
                is PlaceSearchLocalResult.PlaceSearchFailed -> {
                    PlaceSearchResult.PlaceSearchFailed(
                        places = searchIndex.map { it.mapToDomain() },
                        nextPageCursor = pageCursor, error = apiResult.error
                    )
                }

                PlaceSearchLocalResult.PlaceSearchLoading -> {
                    PlaceSearchResult.PlaceSearchLoading(
                        places = searchIndex.map { it.mapToDomain() },
                        nextPageCursor = pageCursor,
                    )
                }

                is PlaceSearchLocalResult.PlaceSearchSuccess -> {
                    PlaceSearchResult.PlaceSearchSuccess(
                        places = searchIndex.map { it.mapToDomain() },
                        nextPageCursor = apiResult.nextPageCursor
                    )
                }
            }
        }

    override fun changePlaceFavouriteStatus(placeId: String): Flow<FavouriteStatusResult> =
        flow<FavouriteStatusResult> {
            emit(
                FavouriteStatusResult.FavouriteStatusSuccess(
                    localDatasource.changeFavouriteStatus(
                        placeId
                    )
                )
            )
        }.onStart { emit(FavouriteStatusResult.FavouriteStatusLoading) }.flowOn(dispatcher)

    override fun getFavouritePlaces(): Flow<PlaceSearchResult> = localDatasource.observeFavourites()
        .map<List<PlaceLocalWithFavourite>, PlaceSearchResult> {
            PlaceSearchResult.PlaceSearchSuccess(
                places = it.map { it.mapToDomain() },
                nextPageCursor = null
            )
        }.onStart {
            emit(
                PlaceSearchResult.PlaceSearchLoading(
                    places = listOf(),
                    nextPageCursor = null
                )
            )
        }.flowOn(dispatcher)

    private fun getPlacesFromCacheAndUpdateIndex(query: String, pageCursor: String?) =
        flow<PlaceSearchLocalResult> {
            if (pageCursor == null) localDatasource.clearSearchIndex()
            localDatasource.searchPlaceLocally(query = query, pageCursor = pageCursor).onSuccess {
                with(it) {
                    localDatasource.updateSearchIndex(places)
                }
                emit(
                    PlaceSearchLocalResult.PlaceSearchSuccess(
                        places = it.places,
                        nextPageCursor = it.nextPageCursor
                    )
                )
            }.onFailure {
                emit(
                    PlaceSearchLocalResult.PlaceSearchFailed(
                        error = PlaceSearchError.GenericError
                    )
                )
            }
        }.onStart { emit(PlaceSearchLocalResult.PlaceSearchLoading) }.flowOn(dispatcher)

    private fun getPlacesFromApiAndCashItLocally(query: String, pageCursor: String?) =
        flow<PlaceSearchLocalResult> {
            if (pageCursor == null) localDatasource.clearSearchIndex()
        remoteDataSource.searchPlace(query = query, pageCursor = pageCursor).onSuccess {
            with(it) {
                localDatasource.cacheSearchResult(places.map { it.mapToLocal() })
                localDatasource.updateSearchIndex(places.map { it.mapToLocal() })
            }
            emit(
                PlaceSearchLocalResult.PlaceSearchSuccess(
                    places = it.places.map { place -> place.mapToLocal() },
                    nextPageCursor = it.nextPageCursor
                )
            )
        }.onFailure {
            emit(
                PlaceSearchLocalResult.PlaceSearchFailed(
                    error = PlaceSearchError.GenericError
                )
            )
        }
    }.onStart { emit(PlaceSearchLocalResult.PlaceSearchLoading) }.flowOn(dispatcher)

    override fun getPlaceDetails(placeId: String): Flow<PlaceDetailsResult> =
        combine<PlaceDetailsLocalResult, PlaceDetailsLocalWithFavourite?, PlaceDetailsResult>(
            flow = if (deviceNetworkDataSource.internetAvailable()) getPlaceDetailsFromApiAndCacheItLocally(
                placeId = placeId
            )
            else getPlaceDetailsFromCache(placeId = placeId),
            flow2 = localDatasource.observePlaceDetails(placeId = placeId)
        ) { apiResult, placeDetails ->
            when (apiResult) {
                is PlaceDetailsLocalResult.PlaceDetailsFailed -> PlaceDetailsResult.PlaceDetailsFailed(
                    error = apiResult.error
                )

                PlaceDetailsLocalResult.PlaceDetailsLoading -> PlaceDetailsResult.PlaceDetailsLoading
                is PlaceDetailsLocalResult.PlaceDetailsSuccess -> PlaceDetailsResult.PlaceDetailsSuccess(
                    placeDetails = placeDetails?.mapToDomain()
                )
            }
        }

    private fun getPlaceDetailsFromApiAndCacheItLocally(placeId: String) =
        flow<PlaceDetailsLocalResult> {
            remoteDataSource.getPlaceDetails(placeId = placeId).onSuccess {
                localDatasource.cachePlaceDetails(place = it.mapToLocal())
                emit(
                    PlaceDetailsLocalResult.PlaceDetailsSuccess(
                        placeDetails = it.mapToLocal()
                    )
                )
            }.onFailure {
                emit(
                    PlaceDetailsLocalResult.PlaceDetailsFailed(
                        //here we can parse the errors from api etc
                        error = PlaceDetailsError.GenericError
                    )
                )
            }
        }.onStart { emit(PlaceDetailsLocalResult.PlaceDetailsLoading) }.flowOn(dispatcher)

    private fun getPlaceDetailsFromCache(placeId: String) =
        flow<PlaceDetailsLocalResult> {
            emit(
                PlaceDetailsLocalResult.PlaceDetailsSuccess(
                    placeDetails = localDatasource.getPlaceDetails(placeId = placeId)
                )
            )
        }.onStart { emit(PlaceDetailsLocalResult.PlaceDetailsLoading) }.flowOn(dispatcher)
}