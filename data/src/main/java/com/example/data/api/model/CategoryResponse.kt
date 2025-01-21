package com.example.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponse(
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