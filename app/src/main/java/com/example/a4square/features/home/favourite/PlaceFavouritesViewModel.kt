package com.example.a4square.features.home.favourite

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a4square.features.home.places_search.PlacesListState
import com.example.domain.entity.result.PlaceSearchResult
import com.example.domain.repository.PlacesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class Query(val searchText: String, val pageCursor: String? = null)

@HiltViewModel
@OptIn(kotlinx.coroutines.FlowPreview::class)
class PlaceFavouritesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val placesRepository: PlacesRepository,
) : ViewModel() {

    private val _favourites: MutableStateFlow<PlacesListState> =
        MutableStateFlow(PlacesListState.empty())
    val favourites: StateFlow<PlacesListState> = _favourites
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PlacesListState.empty()
        )

    init {
        @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
        placesRepository.getFavouritePlaces()
            .onEach {
                _favourites.value = it.reducePage()
            }
            .launchIn(
                scope = viewModelScope,
            )
    }

    fun onChangePlaceFavouriteStatus(placeId: String) {
        placesRepository.changePlaceFavouriteStatus(
            placeId
        ).onEach {
            //show message if failed
        }.launchIn(viewModelScope)
    }

    fun onRetry() {

    }
}

private fun PlaceSearchResult.reducePage(): PlacesListState {
    return when (this) {
        is PlaceSearchResult.PlaceSearchFailed -> PlacesListState.PlacesListStateFailed(
            places = this.places.toImmutableList(),
            nextPageCursor = this.nextPageCursor,
            error = this.error
        )

        is PlaceSearchResult.PlaceSearchLoading -> PlacesListState.PlacesListStateLoading(
            places = this.places.toImmutableList(),
            nextPageCursor = this.nextPageCursor,
        )

        is PlaceSearchResult.PlaceSearchSuccess -> {
            PlacesListState.PlacesListStateLoaded(
                places = this.places.toImmutableList(),
                nextPageCursor = this.nextPageCursor,
            )
        }
    }
}