package com.example.a4square.features.home.place_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.a4square.features.home.DetailsRoute
import com.example.domain.entity.result.FavouriteStatusResult
import com.example.domain.entity.result.PlaceResult
import com.example.domain.repository.PlacesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val placesRepository: PlacesRepository
) : ViewModel() {

    private val placeId = savedStateHandle.toRoute<DetailsRoute>().placeId

    private val _state: MutableStateFlow<PlaceDetailsState> =
        MutableStateFlow(PlaceDetailsState.empty())
    val state: StateFlow<PlaceDetailsState> =
        _state.onStart { onEvent(PlaceDetailsEvent.PlaceDetailsGetDataEvent(placeId = placeId)) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = PlaceDetailsState.empty()
            )


    private fun onEvent(event: PlaceDetailsEvent) {
        when (event) {
            PlaceDetailsEvent.ChangeFavouriteStatusEvent -> {
                changePlaceFavouriteStatus(placeId = placeId, currentState = state.value)
            }

            is PlaceDetailsEvent.PlaceDetailsGetDataEvent -> {
                getPlaceWithId(placeId = event.placeId, currentState = state.value)
            }
        }
    }

    private fun getPlaceWithId(placeId: String, currentState: PlaceDetailsState) {
        viewModelScope.launch {
            placesRepository.getPlaceDetails(placeId = placeId)
                .collect { result ->
                    _state.value =
                        result.reduce(currentState)
                }
        }
    }

    private fun changePlaceFavouriteStatus(placeId: String, currentState: PlaceDetailsState) {
        viewModelScope.launch {
            placesRepository.changePlaceFavouriteStatus(placeId = placeId)
                .collect { result ->
                    _state.value =
                        result.reduce(currentState)
                }
        }
    }
}

fun PlaceResult.reduce(state: PlaceDetailsState): PlaceDetailsState {
    return when (this) {
        PlaceResult.PlaceLoading -> state.copy(place = PlaceResult.PlaceLoading)
        is PlaceResult.PlaceFailed -> state.copy(place = PlaceResult.PlaceFailed(error = error))
        is PlaceResult.PlaceSuccess -> state.copy(
            place = PlaceResult.PlaceSuccess(place = place),
            favouriteStatus = FavouriteStatusResult.FavouriteStatusSuccess(isFavourite = place.isFavourite)
        )
    }
}

fun FavouriteStatusResult.reduce(state: PlaceDetailsState): PlaceDetailsState {
    return when (this) {
        FavouriteStatusResult.FavouriteStatusLoading -> state.copy(favouriteStatus = FavouriteStatusResult.FavouriteStatusLoading)
        is FavouriteStatusResult.FavouriteStatusFailed -> state.copy(
            favouriteStatus = FavouriteStatusResult.FavouriteStatusFailed(
                error = error
            )
        )

        is FavouriteStatusResult.FavouriteStatusSuccess -> state.copy(
            favouriteStatus = FavouriteStatusResult.FavouriteStatusSuccess(
                isFavourite = isFavourite
            )
        )
    }
}