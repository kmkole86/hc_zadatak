package com.example.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//index table for favourite places
@Entity(tableName = FavouritePlaceIdLocal.ENTITY_NAME)
data class FavouritePlaceIdLocal(
    @PrimaryKey @ColumnInfo(name = ID) val id: String,
) {

    companion object {
        const val ENTITY_NAME = "favourite_local"
        const val ID = "fav_id"
    }
}