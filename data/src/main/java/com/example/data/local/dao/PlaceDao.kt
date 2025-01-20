package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.model.FavouritePlaceIdDb
import com.example.data.local.model.PlaceDb
import com.example.data.local.model.PlaceDbSearchIndex
import com.example.data.local.model.PlaceDbWithFavourite
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {

    //favourites
    @Query("SELECT * from ${PlaceDb.ENTITY_NAME} as Place INNER JOIN ${FavouritePlaceIdDb.ENTITY_NAME} AS FavouriteId ON Place.id = FavouriteId.fav_id")
    fun observeAllFavouritePlaces(): Flow<List<PlaceDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteId(placeId: FavouritePlaceIdDb)

    @Query("SELECT EXISTS(SELECT * FROM ${FavouritePlaceIdDb.ENTITY_NAME} WHERE ${FavouritePlaceIdDb.ID}=:placeId)")
    suspend fun getFavouriteStatus(placeId: String): Boolean

    @Query("DELETE FROM ${FavouritePlaceIdDb.ENTITY_NAME} WHERE ${FavouritePlaceIdDb.ID}=:placeId")
    suspend fun deleteFavouriteId(placeId: String)

    //search index
    @Query("SELECT * from ${PlaceDbSearchIndex.ENTITY_NAME} AS SearchPlaceIdex INNER JOIN ${PlaceDb.ENTITY_NAME} as PlaceCache ON SearchPlaceIdex.id = PlaceCache.id LEFT JOIN ${FavouritePlaceIdDb.ENTITY_NAME} AS FavouritePlaceIdex ON SearchPlaceIdex.id = FavouritePlaceIdex.fav_id")
    fun observeSearchIndex(): Flow<List<PlaceDbWithFavourite>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchIndexPlaceId(places: List<PlaceDbSearchIndex>)

    @Query("DELETE FROM ${PlaceDbSearchIndex.ENTITY_NAME}")
    suspend fun clearSearchIndex()

    //places cache
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaces(places: List<PlaceDb>)
}