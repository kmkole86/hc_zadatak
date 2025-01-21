package com.example.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.database.dao.PlaceDao
import com.example.data.database.model.FavouritePlaceIdDb
import com.example.data.database.model.PlaceDb
import com.example.data.database.model.PlaceDbSearchIndex
import com.example.data.database.model.PlaceDbWithFavourite
import com.example.data.database.model.PlaceDetailsDb
import com.example.data.database.model.PlaceDetailsDbWithFavourite

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