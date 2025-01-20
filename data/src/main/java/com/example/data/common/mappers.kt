package com.example.data.common

import com.example.data.local.model.PlaceDb
import com.example.data.local.model.PlaceDbWithFavourite
import com.example.data.model.PlaceLocal
import com.example.data.model.PlaceLocalWithFavourite
import com.example.data.remote.model.PlaceResponse
import com.example.domain.entity.Place

fun PlaceLocal.mapToDb(): PlaceDb =
    PlaceDb(id = id, name = name, distance = distance, iconUrl = iconUrl)

fun PlaceResponse.mapToLocal() =
    PlaceLocal(
        id = id,
        distance = distance,
        name = name,
        iconUrl = categories.firstOrNull()?.icon?.let { "${it.prefix}120${it.suffix}" })

fun PlaceDbWithFavourite.mapToLocal() =
    PlaceLocalWithFavourite(
        place = PlaceLocal(
            id = id,
            name = name,
            distance = distance,
            iconUrl = iconUrl,
        ), isFavourite = favouriteId != null
    )

fun PlaceLocalWithFavourite.mapToDomain(): Place =
    Place(
        id = place.id,
        name = place.name,
        distance = place.distance,
        iconUrl = place.iconUrl,
        isFavourite = isFavourite,
    )