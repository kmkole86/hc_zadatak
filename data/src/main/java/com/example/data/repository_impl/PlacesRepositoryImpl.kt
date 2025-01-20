package com.example.data.repository_impl

import com.example.data.common.mapToDomain
import com.example.data.common.mapToLocal
import com.example.data.local.data_source_impl.PlacesLocalDataSource
import com.example.data.model.PlaceLocalWithFavourite
import com.example.data.model.PlaceSearchLocalResult
import com.example.data.remote.data_source_impl.PlacesRemoteDataSource
import com.example.domain.entity.result.FavouriteStatusResult
import com.example.domain.entity.result.PlaceResult
import com.example.domain.entity.result.PlaceSearchError
import com.example.domain.entity.result.PlaceSearchResult
import com.example.domain.repository.PlacesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
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

    override fun searchPlace(query: String, pageCursor: String?): Flow<PlaceSearchResult> {
        return combine<PlaceSearchLocalResult, List<PlaceLocalWithFavourite>, PlaceSearchResult>(
            flow = searchPlaceApi(query = query, pageCursor = pageCursor),
            flow2 = localDatasource.observeSearchIndex()
        ) { apiResult, searchIndex: List<PlaceLocalWithFavourite> ->
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

    private fun searchPlaceApi(query: String, pageCursor: String?) = flow<PlaceSearchLocalResult> {
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
}