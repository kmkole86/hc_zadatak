package com.example.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = PlaceLocal.ENTITY_NAME)
data class PlaceLocal(
    @PrimaryKey @ColumnInfo(name = ID) val id: String,
    @ColumnInfo(name = NAME) val name: String,
    @ColumnInfo(name = DISTANCE) val distance: Int,
) {

    companion object {
        const val ENTITY_NAME = "places_local"
        const val ID = "id"
        const val NAME = "name"
        const val DISTANCE = "distance"
    }
}