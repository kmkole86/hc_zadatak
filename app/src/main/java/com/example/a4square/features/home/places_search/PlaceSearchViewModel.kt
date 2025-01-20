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

data class Query(val searchText: String, val pageCursor: String? = null)

@HiltViewModel
@OptIn(kotlinx.coroutines.FlowPreview::class)
class PlaceSearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val placesRepository: PlacesRepository,
) : ViewModel() {

    private val _searchText: MutableStateFlow<Query> =
        MutableStateFlow(Query(searchText = ""))
    val searchText: StateFlow<Query> = _searchText

    private val _searchResult: MutableStateFlow<PlacesListState> =
        MutableStateFlow(PlacesListState.empty())
    val searchResult: StateFlow<PlacesListState> = _searchResult
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PlacesListState.empty()
        )

    init {
        @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
        _searchText.debounce(300)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                if (query.searchText.isNotBlank() && query.searchText.length >= 3)
                    placesRepository.searchPlace(
                        query = query.searchText,
                        pageCursor = query.pageCursor
                    )
                else flowOf(
                    PlaceSearchResult.PlaceSearchSuccess.empty()
                )
            }.onEach {
                _searchResult.value = it.reducePage()
            }
            .launchIn(
                scope = viewModelScope,
            )
    }

    fun loadNextPage() {
        with(searchResult.value) {
            if (isLoadingNextPage || nextPageCursor == null) return

            _searchText.value =
                Query(
                    searchText = _searchText.value.searchText,
                    pageCursor = nextPageCursor
                )
        }
    }

    fun onQueryChangedEvent(query: String) {
        _searchText.value = Query(searchText = query)
    }

    fun onChangePlaceFavouriteStatus(placeId: String) {
        placesRepository.changePlaceFavouriteStatus(
            placeId
        ).onEach {
            //show message if failed
        }.launchIn(viewModelScope)
    }

    fun onRetry() {
        _searchText.value =
            Query(
                searchText = _searchText.value.searchText,
                pageCursor = searchResult.value.nextPageCursor
            )
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