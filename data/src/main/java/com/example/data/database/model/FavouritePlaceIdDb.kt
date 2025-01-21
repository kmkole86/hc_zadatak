package com.example.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//index table for favourite places
@Entity(tableName = FavouritePlaceIdDb.ENTITY_NAME)
data class FavouritePlaceIdDb(
    @PrimaryKey @ColumnInfo(name = ID) val id: String,
) {

    companion object {
        const val ENTITY_NAME = "favourite_local"
        const val ID = "fav_id"
    }
}