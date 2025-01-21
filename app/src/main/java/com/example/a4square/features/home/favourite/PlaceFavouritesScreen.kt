package com.example.a4square.features.home.favourite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.a4square.R
import com.example.a4square.features.home.places_search.PlacesListState
import com.example.a4square.features.home.places_search.list.PlaceListErrorItem
import com.example.a4square.features.home.places_search.list.PlaceListInfoItem
import com.example.a4square.features.home.places_search.list.PlaceListItem
import com.example.a4square.features.home.places_search.list.PlaceListLoadingItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceFavouritesScreen(
    modifier: Modifier = Modifier,
    onPlaceClicked: (String) -> Unit,
    viewModel: PlaceFavouritesViewModel = hiltViewModel(),
) {
    val lazyListState = rememberLazyListState()
    val favourites by viewModel.favourites.collectAsStateWithLifecycle()

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center)
                    { Text("Marko Kostic", textAlign = TextAlign.Center) }
                })
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(bottom = dimensionResource(id = R.dimen.spacing_2x)),
                state = lazyListState, modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = dimensionResource(id = R.dimen.spacing_2x))
                    .weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                //show place search result items if any
                items(
                    favourites.places.size,
                    key = { index -> favourites.places[index].id },
                    itemContent = { index ->
                        PlaceListItem(
                            place = favourites.places[index],
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
                when (favourites) {
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
                        if (favourites.places.isEmpty()) {
                            item {
                                PlaceListInfoItem(
                                    message = "No favourite places",
                                    modifier = modifier
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}