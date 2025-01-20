package com.example.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlaceDbWithFavourite(
    @PrimaryKey @ColumnInfo(name = PlaceDb.ID) val id: String,
    @ColumnInfo(name = PlaceDb.NAME) val name: String,
    @ColumnInfo(name = PlaceDb.DISTANCE) val distance: Int,
    @ColumnInfo(name = FavouritePlaceIdDb.ID) val favouriteId: Int?,
)