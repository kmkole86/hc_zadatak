package com.example.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlaceDetailsDbWithFavourite(
    @PrimaryKey @ColumnInfo(name = PlaceDb.ID) val id: String,
    @ColumnInfo(name = PlaceDetailsDb.NAME) val name: String,
    @ColumnInfo(name = PlaceDetailsDb.ICON_URL) val iconUrl: String?,
    @ColumnInfo(name = PlaceDetailsDb.CLOSED_BUCKET) val closedBucket: String?,
    @ColumnInfo(name = PlaceDetailsDb.LINK) val link: String?,
    @ColumnInfo(name = PlaceDetailsDb.TIME_ZONE) val timeZone: String?,
    @ColumnInfo(name = FavouritePlaceIdDb.ID) val favouriteId: Int?,
)