package com.example.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = PlaceDetailsDb.ENTITY_NAME)
data class PlaceDetailsDb(
    @PrimaryKey @ColumnInfo(name = ID) val id: String,
    @ColumnInfo(name = NAME) val name: String,
    @ColumnInfo(name = ICON_URL) val iconUrl: String?,
    @ColumnInfo(name = CLOSED_BUCKET) val closedBucket: String?,
    @ColumnInfo(name = LINK) val link: String?,
    @ColumnInfo(name = TIME_ZONE) val timeZone: String?,
) {

    companion object {
        const val ENTITY_NAME = "places_details_cache_table"
        const val ID = "id"
        const val NAME = "name"
        const val ICON_URL = "iconUrl"
        const val CLOSED_BUCKET = "closedBucket"
        const val LINK = "link"
        const val TIME_ZONE = "timeZone"
    }
}