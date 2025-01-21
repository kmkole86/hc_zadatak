package com.example.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//current search index table
@Entity(tableName = PlaceDbSearchIndex.ENTITY_NAME)
data class PlaceDbSearchIndex(
    @PrimaryKey @ColumnInfo(name = ID) val id: String,
) {

    companion object {
        const val ENTITY_NAME = "search_index_table"
        const val ID = "id"
    }
}