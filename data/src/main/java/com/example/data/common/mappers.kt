package com.example.data.common

import com.example.data.local.model.PlaceDb
import com.example.data.local.model.PlaceDbWithFavourite
import com.example.data.local.model.PlaceDetailsDb
import com.example.data.local.model.PlaceDetailsDbWithFavourite
import com.example.data.model.PlaceDetailsLocal
import com.example.data.model.PlaceDetailsLocalWithFavourite
import com.example.data.model.PlaceLocal
import com.example.data.model.result.PlaceLocalWithFavourite
import com.example.data.remote.model.PlaceDetailsResponse
import com.example.data.remote.model.PlaceResponse
import com.example.domain.entity.Place
import com.example.domain.entity.PlaceDetails

fun PlaceLocal.mapToDb(): PlaceDb =
    PlaceDb(id = id, name = name, distance = distance, iconUrl = iconUrl)

fun PlaceResponse.mapToLocal() =
    PlaceLocal(
        id = id,
        distance = distance,
        name = name,
        iconUrl = categories?.firstOrNull()?.icon?.let { "${it.prefix}120${it.suffix}" })

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

fun PlaceDetailsResponse.mapToLocal() = PlaceDetailsLocal(
    id = id,
    name = name,
    iconUrl = categories?.firstOrNull()?.icon?.let { "${it.prefix}120${it.suffix}" },
    link = link,
    closedBucket = closedBucket,
    timeZone = timeZone
)

fun PlaceDetailsDbWithFavourite.mapToLocal() = PlaceDetailsLocalWithFavourite(
    place = PlaceDetailsLocal(
        id = id,
        name = name,
        iconUrl = iconUrl,
        link = link,
        closedBucket = closedBucket,
        timeZone = timeZone
    ), isFavourite = favouriteId != null
)

fun PlaceDetailsLocalWithFavourite.mapToDomain() = PlaceDetails(
    id = place.id,
    name = place.name,
    iconUrl = place.iconUrl,
    link = place.link,
    closedBucket = place.closedBucket,
    timeZone = place.timeZone,
    isFavourite = isFavourite
)


fun PlaceDetailsLocal.mapToDb() = PlaceDetailsDb(
    id = id,
    name = name,
    iconUrl = iconUrl,
    link = link,
    closedBucket = closedBucket,
    timeZone = timeZone
)


//s obzirom da se