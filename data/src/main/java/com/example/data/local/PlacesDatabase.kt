package com.example.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.local.dao.PlaceDao
import com.example.data.local.model.PlaceLocal

@Database(
    version = 1,
    exportSchema = false,
    entities = [
        PlaceLocal::class
    ]
)
abstract class PlacesDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME: String = "places_database"
    }

    abstract fun placesDao(): PlaceDao
}