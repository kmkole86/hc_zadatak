package com.example.a4square.features.home.places_search

import androidx.compose.runtime.Stable
import com.example.domain.entity.Place
import com.example.domain.entity.result.PlaceSearchError
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Stable
sealed class PlacesListState {

    companion object {
        fun empty() = PlacesListStateLoaded(places = persistentListOf(), nextPageCursor = null)
    }

    val hasNextPage: Boolean
        get() = nextPageCursor != null

    val isLoadingNextPage: Boolean
        get() = this is PlacesListStateLoading

    abstract val places: ImmutableList<Place>
    abstract val nextPageCursor: String?

    data class PlacesListStateLoading(
        override val places: ImmutableList<Place>,
        override val nextPageCursor: String?,
    ) : PlacesListState()

    data class PlacesListStateFailed(
        override val places: ImmutableList<Place>,
        override val nextPageCursor: String?,
        val error: PlaceSearchError?
    ) : PlacesListState()

    data class PlacesListStateLoaded(
        override val places: ImmutableList<Place>,
        override val nextPageCursor: String?,
    ) : PlacesListState()
}

//TODO @STABLE