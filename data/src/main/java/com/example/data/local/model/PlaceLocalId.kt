package com.example.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//current search index table
@Entity(tableName = PlaceIdLocal.ENTITY_NAME)
data class PlaceIdLocal(
    @PrimaryKey @ColumnInfo(name = ID) val id: String,
) {

    companion object {
        const val ENTITY_NAME = "search_index_table"
        const val ID = "id"
    }
}