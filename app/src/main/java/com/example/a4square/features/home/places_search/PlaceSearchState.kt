package com.example.a4square.features.home.places_search

import androidx.compose.runtime.Stable
import com.example.domain.entity.Place
import com.example.domain.entity.result.PlaceSearchError
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Stable
sealed class PlacesListState {

    abstract val places: ImmutableList<Place>
    val nextPageCursor: String?

    data class PlacesListStateLoading(
        override val places: ImmutableList<Place>,
    ) : PlacesListState()

    data class PlacesListStateFailed(
        override val places: ImmutableList<Place>,
        val error: PlaceSearchError?
    ) : PlacesListState()

    data class PlacesListStateLoaded(
        override val places: ImmutableList<Place>,
    ) : PlacesListState()

    data class PlacesListStateEndOfList(
        override val places: ImmutableList<Place>,
    ) : PlacesListState()
}

//TODO @STABLE