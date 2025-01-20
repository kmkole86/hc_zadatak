package com.example.a4square.features.home.places_search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.result.PlaceSearchResult
import com.example.domain.repository.PlacesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
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

    private val _searchResult: MutableStateFlow<PlacesListState> =
        MutableStateFlow(PlacesListState.PlacesListStateLoaded(places = persistentListOf()))
    val searchResult: StateFlow<PlacesListState> = _searchResult
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PlacesListState.PlacesListStateLoaded(places = persistentListOf())
        )

    private var nextPageCursor: String? = null

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

    private fun reducePlaceResult(result: PlaceSearchResult) {
        when (result) {
            is PlaceSearchResult.PlaceSearchFailed -> _searchResult.value =
                PlacesListState.PlacesListStateFailed(
                    places = _searchResult.value.places,
                    error = result.error
                )

            PlaceSearchResult.PlaceSearchLoading -> _searchResult.value =
                PlacesListState.PlacesListStateLoading(
                    places = _searchResult.value.places,
                )

            is PlaceSearchResult.PlaceSearchSuccess -> {
                nextPageCursor = result.page.nextPageCursor

                if (nextPageCursor != null) {
                    _searchResult.value = PlacesListState.PlacesListStateLoaded(
                        places = _searchResult.value.places.plus(result.page.places)
                            .toImmutableList(),
                    )
                } else {
                    _searchResult.value = PlacesListState.PlacesListStateEndOfList(
                        places = _searchResult.value.places.plus(result.page.places)
                            .toImmutableList(),
                    )
                }
            }
        }
    }

    fun loadNextPage() {
        if (nextPageCursor == null) return

        placesRepository.searchPlace(
            query = searchResult.value.toString(),
            pageCursor = nextPageCursor
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

    fun onQueryChangedEvent(query: String) {
        _searchText.value = query
    }

    fun onRetry() {
        placesRepository.searchPlace(
            query = searchResult.value.toString(),
            pageCursor = nextPageCursor
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