package com.example.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.local.dao.PlaceDao
import com.example.data.local.model.FavouritePlaceIdDb
import com.example.data.local.model.PlaceDb
import com.example.data.local.model.PlaceDbSearchIndex
import com.example.data.local.model.PlaceDbWithFavourite
import com.example.data.local.model.PlaceDetailsDb
import com.example.data.local.model.PlaceDetailsDbWithFavourite

@Database(
    version = 1,
    exportSchema = false,
    entities = [
        PlaceDb::class,
        PlaceDbSearchIndex::class,
        FavouritePlaceIdDb::class,
        PlaceDbWithFavourite::class,
        PlaceDetailsDb::class,
        PlaceDetailsDbWithFavourite::class,
    ]
)
abstract class PlacesDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME: String = "places_database"
    }

    abstract fun placesDao(): PlaceDao
}