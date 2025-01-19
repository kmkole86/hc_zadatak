package com.example.a4square.features.home.places_search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.result.PlaceSearchResult
import com.example.domain.repository.PlacesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
@OptIn(kotlinx.coroutines.FlowPreview::class)
class PlaceSearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val placesRepository: PlacesRepository,
) : ViewModel() {

    private val _searchText: MutableStateFlow<String> =
        MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private val _searchResult: MutableStateFlow<PlacesListState> =
        MutableStateFlow(PlacesListState.PlacesListStateIdle())
    val searchResult: StateFlow<PlacesListState> = _searchResult
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PlacesListState.PlacesListStateIdle()
        )

    private var nextPageCursor: String? = null

    init {
        @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
        _searchText.debounce(300)
            .distinctUntilChanged()
            .onEach { query -> if (query.isBlank() || query.length <= 2) clearSearchList() }
            .filter { query -> query.isNotBlank() && query.length > 2 }
            .flatMapLatest {
                placesRepository.searchPlace(query = it)
            }.onEach {
                when (it) {
                    is PlaceSearchResult.PlaceSearchFailed -> _searchResult.value =
                        PlacesListState.PlacesListStateFailed(
                            places = _searchResult.value.places,
                            error = it.error
                        )

                    PlaceSearchResult.PlaceSearchLoading -> _searchResult.value =
                        PlacesListState.PlacesListStateLoading(
                            places = _searchResult.value.places,
                        )

                    is PlaceSearchResult.PlaceSearchSuccess -> {
                        nextPageCursor = it.page.nextPageCursor

                        if (nextPageCursor != null) {
                            PlacesListState.PlacesListStateLoading(
                                places = _searchResult.value.places.plus(it.page.places)
                                    .toImmutableList(),
                            )
                        } else {
                            PlacesListState.PlacesListStateLoaded(
                                places = _searchResult.value.places.plus(it.page.places)
                                    .toImmutableList(),
                            )
                        }
                    }
                }
            }
            .launchIn(
                scope = viewModelScope,
            )
    }


    fun loadNextPage() {
        if (nextPageCursor == null) return

        placesRepository.searchPlace(
            query = searchResult.value.toString(),
            pageCursor = nextPageCursor
        ).onEach {
            when (it) {
                is PlaceSearchResult.PlaceSearchFailed -> _searchResult.value =
                    PlacesListState.PlacesListStateFailed(
                        places = _searchResult.value.places,
                        error = it.error
                    )

                PlaceSearchResult.PlaceSearchLoading -> _searchResult.value =
                    PlacesListState.PlacesListStateLoading(
                        places = _searchResult.value.places,
                    )

                is PlaceSearchResult.PlaceSearchSuccess -> {
                    nextPageCursor = it.page.nextPageCursor

                    if (nextPageCursor != null) {
                        PlacesListState.PlacesListStateLoading(
                            places = _searchResult.value.places.plus(it.page.places)
                                .toImmutableList(),
                        )
                    } else {
                        PlacesListState.PlacesListStateLoaded(
                            places = _searchResult.value.places.plus(it.page.places)
                                .toImmutableList(),
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onQueryChangedEvent(query: String) {
        _searchText.value = query
    }

    fun onRetry() {

    }

    fun clearSearchList() {
        _searchResult.value =
            PlacesListState.PlacesListStateIdle()
    }
}

//fun PlaceSearchResult.reduceToPlacesListState(searchResultState: PlacesListState): PlacesListState {
//    return when (this) {
//        is PlaceSearchResult.PlaceSearchFailed -> PlacesListState.PlacesListStateFailed(
//            places = searchResultState.places,
//            error = error
//        )
//
//        PlaceSearchResult.PlaceSearchLoading -> PlacesListState.PlacesListStateLoading(
//            places = searchResultState.places
//        )
//
//        is PlaceSearchResult.PlaceSearchSuccess -> {}
//
//
//        PlacesListState.PlacesListStateLoaded(
//            places = searchResultState.places + this.page
//        )
//    }
//}

//fun FavouriteStatusResult.reduce(state: PlaceDetailsState): PlaceSearchState {
//    return when (this) {
//        FavouriteStatusResult.FavouriteStatusLoading -> state.copy(favouriteStatus = FavouriteStatusResult.FavouriteStatusLoading)
//        is FavouriteStatusResult.FavouriteStatusFailed -> state.copy(
//            favouriteStatus = FavouriteStatusResult.FavouriteStatusFailed(
//                error = error
//            )
//        )
//
//        is FavouriteStatusResult.FavouriteStatusSuccess -> state.copy(
//            favouriteStatus = FavouriteStatusResult.FavouriteStatusSuccess(
//                isFavourite = isFavourite
//            )
//        )
//    }
//}