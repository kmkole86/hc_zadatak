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
                if (query.isNotBlank() && query.length >= 3)
                    placesRepository.searchPlace(query = query)
                else flowOf(PlaceSearchResult.PlaceSearchSuccess.empty())
            }.onEach {
                reduceFirstPageResult(it)
            }
            .launchIn(
                scope = viewModelScope,
            )

//        viewModelScope.launch {
//            observeCachedAuthorUseCase.observeAuthor(authorId = authorId).collect {
//                reduce(it)
//            }
//        }
    }

    private fun reduceFirstPageResult(data: PlaceSearchResult) {
        _searchResult.value = data.reduceFirstPage(currentState = _searchResult.value)
    }

    private fun reduceNextPageResult(data: PlaceSearchResult) {
        _searchResult.value = data.reduceNextPage(currentState = _searchResult.value)
    }

    fun loadNextPage() {
        with(_searchResult.value) {
            if (nextPageCursor == null) return

            placesRepository.searchPlace(
                query = searchResult.value.toString(),
                pageCursor = nextPageCursor
            ).onEach {
                reduceNextPageResult(it)
            }.launchIn(viewModelScope)
        }
    }

    fun onQueryChangedEvent(query: String) {
        _searchText.value = query.trim()
    }

    fun onChangePlaceFavouriteStatus(placeId: String) {
        placesRepository.changePlaceFavouriteStatus(
            placeId
        ).onEach {
            //show message if failed
        }.launchIn(viewModelScope)
    }

    fun onRetry() {
        placesRepository.searchPlace(
            query = searchResult.value.toString(),
            pageCursor = _searchResult.value.nextPageCursor
        ).onEach {
            if (searchResult.value.nextPageCursor == null) reduceFirstPageResult(it)
            else reduceNextPageResult(it)
        }.launchIn(viewModelScope)
    }
}

private fun PlaceSearchResult.reduceFirstPage(currentState: PlacesListState): PlacesListState {
    return when (this) {
        is PlaceSearchResult.PlaceSearchFailed -> PlacesListState.PlacesListStateFailed(
            places = persistentListOf(),
            nextPageCursor = null,
            error = this.error
        )

        PlaceSearchResult.PlaceSearchLoading -> PlacesListState.PlacesListStateLoading(
            places = persistentListOf(),
            nextPageCursor = null,
        )

        is PlaceSearchResult.PlaceSearchSuccess -> {
//            if (this.page.nextPageCursor != null) {
//                PlacesListState.PlacesListStateLoading(
//                    places = currentState.places.plus(this.page.places)
//                        .toImmutableList(),
//                    nextPageCursor = this.page.nextPageCursor
//                )
//            } else {
            PlacesListState.PlacesListStateLoaded(
                places = currentState.places.plus(this.page.places)
                    .toImmutableList(),
                nextPageCursor = this.page.nextPageCursor
            )
//            }
        }
    }
}

private fun PlaceSearchResult.reduceNextPage(currentState: PlacesListState): PlacesListState {
    return when (this) {
        is PlaceSearchResult.PlaceSearchFailed -> PlacesListState.PlacesListStateFailed(
            places = currentState.places,
            nextPageCursor = currentState.nextPageCursor,
            error = this.error
        )

        PlaceSearchResult.PlaceSearchLoading -> PlacesListState.PlacesListStateLoading(
            places = currentState.places, nextPageCursor = currentState.nextPageCursor
        )

        is PlaceSearchResult.PlaceSearchSuccess -> {
//            if (this.page.nextPageCursor != null) {
//                PlacesListState.PlacesListStateLoading(
//                    places = currentState.places.plus(this.page.places)
//                        .toImmutableList(),
//                    nextPageCursor = this.page.nextPageCursor
//                )
//            } else {
            PlacesListState.PlacesListStateLoaded(
                places = currentState.places.plus(this.page.places).toImmutableList(),
                nextPageCursor = this.page.nextPageCursor
            )
//            }
        }
    }
}