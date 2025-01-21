package com.example.data.model

//posto dolazi sa neta a isFavourite je lokalni, it doesnt have isFavourite.
//ili da se cuva kao deo place cache-a, ali onda ima dva mesta gde se updatuje local i localdetails
//moze i da se isFavourite stavi tu, i da bude false kada stize sa neta, ali onda
//mora da se pazi da se ne overriduje u cache-u
data class PlaceDetailsLocal(
    val id: String,
    val name: String,
    val iconUrl: String?,
    val closedBucket: String?,
    val link: String?,
    val timeZone: String?,
)