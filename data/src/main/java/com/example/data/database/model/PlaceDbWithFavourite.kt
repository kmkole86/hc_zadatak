package com.example.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlaceDbWithFavourite(
    @PrimaryKey @ColumnInfo(name = PlaceDb.ID) val id: String,
    @ColumnInfo(name = PlaceDb.NAME) val name: String,
    @ColumnInfo(name = PlaceDb.DISTANCE) val distance: Int,
    @ColumnInfo(name = PlaceDb.ICON_URL) val iconUrl: String?,
    @ColumnInfo(name = FavouritePlaceIdDb.ID) val favouriteId: Int?,
)