package com.example.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceResponse(
    @SerialName(value = "fsq_id")
    val id: String,
    @SerialName(value = "distance")
    val distance: Int,
    @SerialName(value = "name")
    val name: String,
    @SerialName(value = "categories")
    val categories: List<Category>

) {
    @Serializable
    data class Category(
        @SerialName(value = "icon")
        val icon: CategoryIcon,
    ) {
        @Serializable
        data class CategoryIcon(
            @SerialName(value = "prefix")
            val prefix: String,
            @SerialName(value = "suffix")
            val suffix: String,
        )
    }
}