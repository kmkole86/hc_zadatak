package com.example.a4square.features.home.place_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.a4square.features.home.DetailsRoute
import com.example.domain.entity.result.PlaceDetailsResult
import com.example.domain.repository.PlacesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PlaceDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val placesRepository: PlacesRepository
) : ViewModel() {

    private val placeId = savedStateHandle.toRoute<DetailsRoute>().placeId

    private val _placeDetails: MutableStateFlow<PlaceDetailsState> =
        MutableStateFlow(PlaceDetailsState.PlaceDetailsLoading)
    val placeDetails: StateFlow<PlaceDetailsState> =
        _placeDetails.onStart { getPlaceDetails(placeId = placeId) }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
            initialValue = PlaceDetailsState.PlaceDetailsLoading
            )

    fun onChangePlaceFavouriteStatus(placeId: String) {
        placesRepository.changePlaceFavouriteStatus(
            placeId
        ).onEach {
            //handle error here, emit toast...
            //if there is a real api call
            //for local impl we are using there is no error possible
        }.launchIn(viewModelScope)
    }

    fun onRetry() {
        getPlaceDetails(placeId = placeId)
    }

    private fun getPlaceDetails(placeId: String) {
        placesRepository.getPlaceDetails(placeId = placeId)
            .onEach {
                _placeDetails.value = it.reduce()
            }
            .launchIn(
                scope = viewModelScope,
            )
    }
}

fun PlaceDetailsResult.reduce(): PlaceDetailsState = when (this) {
    is PlaceDetailsResult.PlaceDetailsFailed -> PlaceDetailsState.PlaceDetailsFailed(error = error)
    PlaceDetailsResult.PlaceDetailsLoading -> PlaceDetailsState.PlaceDetailsLoading
    is PlaceDetailsResult.PlaceDetailsSuccess -> PlaceDetailsState.PlaceDetailsSuccess(placeDetails = placeDetails)
}