package com.example.a4square.features.home.places_search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.a4square.features.home.places_search.list.OnBottomReached
import com.example.a4square.features.home.places_search.list.PlaceListErrorItem
import com.example.a4square.features.home.places_search.list.PlaceListInfoItem
import com.example.a4square.features.home.places_search.list.PlaceListItem
import com.example.a4square.features.home.places_search.list.PlaceListLoadingItem

@Composable
fun PlaceSearchScreen(
    modifier: Modifier = Modifier,
    onPlaceClicked: (String) -> Unit,
    viewModel: PlaceSearchViewModel = hiltViewModel(),
) {
    val lazyListState = rememberLazyListState()
    val query by viewModel.searchText.collectAsStateWithLifecycle()
    val searchResult by viewModel.searchResult.collectAsStateWithLifecycle()

    lazyListState.OnBottomReached {
        viewModel::loadNextPage.invoke()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = query.searchText,
            onValueChange = viewModel::onQueryChangedEvent,
            modifier = modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (query.searchText.length < 3)
            PlaceListInfoItem(
                message = "Type 3 or more chars to start search...",
                modifier = modifier
            )
        else
            LazyColumn(
                state = lazyListState, modifier = modifier
                    .fillMaxSize()
                    .weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                //show place result items if any
                items(
                    searchResult.places.size,
                    key = { index -> searchResult.places[index].id },
                    itemContent = { index ->
                        PlaceListItem(
                            place = searchResult.places[index],
                            onPlaceClicked = onPlaceClicked,
                            onFavouriteClicked = viewModel::onChangePlaceFavouriteStatus,
                            modifier = modifier
                        )
                    })
                //add extra (info) item if necessary

                //to avoid blocking UI, and to give user feedback at any moment
                //cases like loading of the next page,
                //or error when next page fetch has failed
                //is implemented with "extra" item at the end of the list
                //PlaceListLoadingItem: when there is next page available or page is fetching
                //PlaceListErrorItem: when there is an error fetching next page
                when (searchResult) {
                    is PlacesListState.PlacesListStateLoading -> {
                        item {
                            PlaceListLoadingItem(modifier = modifier)
                        }
                    }

                    is PlacesListState.PlacesListStateFailed -> {
                        item {
                            PlaceListErrorItem(
                                onRetryClicked = viewModel::onRetry,
                                modifier = modifier
                            )
                        }
                    }

                    is PlacesListState.PlacesListStateLoaded -> {
                        if (searchResult.places.isEmpty()) {
                            item {
                                PlaceListInfoItem(
                                    message = "No result",
                                    modifier = modifier
                                )
                            }
                        } else if (searchResult.hasNextPage) {
                            item {
                                PlaceListLoadingItem(modifier = modifier)
                            }
                        }
                    }
                }
            }
    }
}