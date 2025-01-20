package com.example.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlaceLocalWithFavourite(
    @PrimaryKey @ColumnInfo(name = PlaceLocal.ID) val id: String,
    @ColumnInfo(name = PlaceLocal.NAME) val name: String,
    @ColumnInfo(name = PlaceLocal.DISTANCE) val distance: Int,
    @ColumnInfo(name = FavouritePlaceIdLocal.ID) val favouriteId: Int?,
)