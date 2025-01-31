package com.example.data.database.data_source_impl

import com.example.data.common.mapToDb
import com.example.data.common.mapToLocal
import com.example.data.database.dao.PlaceDao
import com.example.data.database.model.FavouritePlaceIdDb
import com.example.data.database.model.PlaceDbSearchIndex
import com.example.data.model.PlaceDetailsLocal
import com.example.data.model.PlaceDetailsLocalWithFavourite
import com.example.data.model.PlaceLocal
import com.example.data.model.PlacePageLocal
import com.example.data.model.result.PlaceLocalWithFavourite
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface PlacesLocalDataSource {

    suspend fun cacheSearchResult(places: List<PlaceLocal>)

    suspend fun changeFavouriteStatus(placeId: String): Boolean

    suspend fun clearSearchIndex()

    suspend fun updateSearchIndex(places: List<PlaceLocal>)

    fun observeSearchIndex(): Flow<List<PlaceLocalWithFavourite>>

    fun observeFavourites(): Flow<List<PlaceLocalWithFavourite>>

    fun observePlaceDetails(placeId: String): Flow<PlaceDetailsLocalWithFavourite?>

    suspend fun cachePlaceDetails(place: PlaceDetailsLocal)

    suspend fun searchPlaceLocally(query: String, pageCursor: String?): Result<PlacePageLocal>

    fun getPlaceDetails(placeId: String): PlaceDetailsLocal?
}

class PlacesLocalDataSourceImpl @Inject constructor(private val dao: PlaceDao) :
    PlacesLocalDataSource {

    override suspend fun cacheSearchResult(places: List<PlaceLocal>) {
        dao.insertPlaces(places.map { it.mapToDb() })
    }

    override suspend fun changeFavouriteStatus(placeId: String): Boolean {
        if (dao.getFavouriteStatus(placeId = placeId))
            dao.deleteFavouriteId(placeId)
        else
            dao.insertFavouriteId(FavouritePlaceIdDb(placeId))

        return dao.getFavouriteStatus(placeId = placeId)
    }

    override suspend fun clearSearchIndex() {
        dao.clearSearchIndex()
    }

    override suspend fun updateSearchIndex(places: List<PlaceLocal>) {
        dao.insertSearchIndexPlaceId(places.map { PlaceDbSearchIndex(id = it.id) })
    }

    override fun observeSearchIndex(): Flow<List<PlaceLocalWithFavourite>> =
        dao.observeSearchIndex().distinctUntilChanged().map { it.map { it.mapToLocal() } }

    override fun observeFavourites(): Flow<List<PlaceLocalWithFavourite>> =
        dao.observeFavourites().distinctUntilChanged().map { it.map { it.mapToLocal() } }

    override fun observePlaceDetails(placeId: String): Flow<PlaceDetailsLocalWithFavourite?> =
        dao.observePlaceDetails(placeId = placeId).distinctUntilChanged().map { it?.mapToLocal() }

    override suspend fun cachePlaceDetails(place: PlaceDetailsLocal) {
        dao.insertPlaceDetails(place.mapToDb())
    }

    override suspend fun searchPlaceLocally(
        query: String,
        pageCursor: String?
    ): Result<PlacePageLocal> {
        return Result.success(
            PlacePageLocal(
                places = dao.searchPlaceCache(query = query).map { it.mapToLocal() },
                nextPageCursor = null
            )
        )
    }

    override fun getPlaceDetails(placeId: String): PlaceDetailsLocal? =
        dao.getPlaceDetails(placeId = placeId)?.mapToLocal()
}