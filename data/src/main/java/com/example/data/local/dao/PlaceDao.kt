package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.model.FavouritePlaceIdLocal
import com.example.data.local.model.PlaceIdLocal
import com.example.data.local.model.PlaceLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {

    //favourites
    @Query("SELECT * from ${PlaceLocal.ENTITY_NAME} as Place INNER JOIN ${FavouritePlaceIdLocal.ENTITY_NAME} AS FavouriteId ON Place.id = FavouriteId.fav_id")
    fun observeAllFavouritePlaces(): Flow<List<PlaceLocal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteId(placeId: FavouritePlaceIdLocal)

    @Query("DELETE FROM ${FavouritePlaceIdLocal.ENTITY_NAME} WHERE ${FavouritePlaceIdLocal.ID}=:placeId")
    suspend fun deleteFavouriteId(placeId: String)

    //search index
    @Query("SELECT * from ${PlaceIdLocal.ENTITY_NAME} AS SearchPlaceIdex INNER JOIN ${PlaceLocal.ENTITY_NAME} as PlaceCache ON SearchPlaceIdex.id = PlaceCache.id LEFT JOIN ${FavouritePlaceIdLocal.ENTITY_NAME} AS FavouritePlaceIdex ON SearchPlaceIdex.id = FavouritePlaceIdex.fav_id")
    fun observeSearchIndex(): Flow<List<PlaceLocal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchIndexPlaceId(places: List<PlaceLocal>)

    @Query("DELETE FROM ${PlaceLocal.ENTITY_NAME}")
    suspend fun clearSearchIndex()

    //places cache
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaces(places: List<PlaceLocal>)
}