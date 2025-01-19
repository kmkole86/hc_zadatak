package com.example.a4square.features.home.places_search

import androidx.compose.runtime.Stable
import com.example.domain.entity.Place
import com.example.domain.entity.result.PlaceSearchError
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

//data class PlaceSearchState(val query: String, val result: PlacesListState) {
//    companion object {
//        fun empty() = PlaceSearchState(
//            query = "",
//            result = PlacesListState.PlacesListStateLoading(places = persistentListOf())
//        )
//    }
//}

@Stable
sealed class PlacesListState {

    abstract val places: ImmutableList<Place>

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

//    data class PlacesListStateEmpty(
//        override val places: ImmutableList<Place>,
//    ) : PlacesListState()

    data class PlacesListStateIdle(
        override val places: ImmutableList<Place> = persistentListOf(),
    ) : PlacesListState()
}

val PlacesListState.hasNextPage: Boolean
    get() = this !is PlacesListState.PlacesListStateEndOfList

//TODO @STABLE