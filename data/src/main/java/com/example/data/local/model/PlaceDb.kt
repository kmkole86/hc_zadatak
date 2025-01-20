package com.example.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//cached table where all fetched places are stored
@Entity(tableName = PlaceDb.ENTITY_NAME)
data class PlaceDb(
    @PrimaryKey @ColumnInfo(name = ID) val id: String,
    @ColumnInfo(name = NAME) val name: String,
    @ColumnInfo(name = DISTANCE) val distance: Int,
    @ColumnInfo(name = ICON_URL) val iconUrl: String?,
) {

    companion object {
        const val ENTITY_NAME = "places_cache_table"
        const val ID = "id"
        const val NAME = "name"
        const val DISTANCE = "distance"
        const val ICON_URL = "iconUrl"
    }
}