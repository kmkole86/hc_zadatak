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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
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
//    private val searchTextState
//        get() = _searchText.value

    private val _searchResult: MutableStateFlow<PlacesListState> =
        MutableStateFlow(PlacesListState.empty())
    val searchResult: StateFlow<PlacesListState> = _searchResult
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PlacesListState.empty()
        )
//    private val searchResultState
//        get() = _searchResult.value

    init {
        @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
        _searchText.debounce(300)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                if (query.isNotBlank() && query.length > 2)
                    placesRepository.searchPlace(query = query)
                else flowOf(PlaceSearchResult.PlaceSearchSuccess.empty())
            }.onEach {
                reducePlaceResult(it)
            }
            .launchIn(
                scope = viewModelScope,
            )
    }

    private fun reducePlaceResult(data: PlaceSearchResult) {
        _searchResult.value = data.reduce(currentState = _searchResult.value)
    }

    fun loadNextPage() {
        with(_searchResult.value) {
            if (nextPageCursor == null) return

            placesRepository.searchPlace(
                query = searchResult.value.toString(),
                pageCursor = nextPageCursor
            ).onEach {
                reducePlaceResult(it)
            }.launchIn(viewModelScope)
        }
//        if (_searchResult.value.nextPageCursor == null) return
//
//        placesRepository.searchPlace(
//            query = searchResult.value.toString(),
//            pageCursor = nextPageCursor
//        ).onEach {
//            reducePlaceResult(it)
//        }.launchIn(viewModelScope)
    }

    fun onQueryChangedEvent(query: String) {
        _searchText.value = query
    }

    fun onRetry() {
        placesRepository.searchPlace(
            query = searchResult.value.toString(),
            pageCursor = _searchResult.value.nextPageCursor
        ).onEach {
            reducePlaceResult(it)
//            when (it) {
//                is PlaceSearchResult.PlaceSearchFailed -> _searchResult.value =
//                    PlacesListState.PlacesListStateFailed(
//                        places = _searchResult.value.places,
//                        error = it.error
//                    )
//
//                PlaceSearchResult.PlaceSearchLoading -> _searchResult.value =
//                    PlacesListState.PlacesListStateLoading(
//                        places = _searchResult.value.places,
//                    )
//
//                is PlaceSearchResult.PlaceSearchSuccess -> {
//                    nextPageCursor = it.page.nextPageCursor
//
//                    if (nextPageCursor != null) {
//                        _searchResult.value = PlacesListState.PlacesListStateLoading(
//                            places = _searchResult.value.places.plus(it.page.places)
//                                .toImmutableList(),
//                        )
//                    } else {
//                        _searchResult.value = PlacesListState.PlacesListStateLoaded(
//                            places = _searchResult.value.places.plus(it.page.places)
//                                .toImmutableList(),
//                        )
//                    }
//                }
//            }
        }.launchIn(viewModelScope)
    }
}

private fun PlaceSearchResult.reduce(currentState: PlacesListState): PlacesListState {
    return when (this) {
        is PlaceSearchResult.PlaceSearchFailed -> PlacesListState.PlacesListStateFailed(
            places = currentState.places,
            nextPageCursor = currentState.nextPageCursor,
            error = this.error
        )

        PlaceSearchResult.PlaceSearchLoading -> PlacesListState.PlacesListStateLoading(
            places = currentState.places,
            nextPageCursor = currentState.nextPageCursor
        )

        is PlaceSearchResult.PlaceSearchSuccess -> {
            if (this.page.nextPageCursor != null) {
                PlacesListState.PlacesListStateLoading(
                    places = currentState.places.plus(this.page.places)
                        .toImmutableList(),
                    nextPageCursor = this.page.nextPageCursor
                )
            } else {
                PlacesListState.PlacesListStateLoaded(
                    places = currentState.places.plus(this.page.places)
                        .toImmutableList(),
                    nextPageCursor = this.page.nextPageCursor
                )
            }
        }
    }
}