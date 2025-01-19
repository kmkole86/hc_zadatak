package com.example.a4square.features.home.places_search

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.a4square.features.home.places_search.list.OnBottomReached
import com.example.a4square.features.home.places_search.list.PlacesList

@Composable
fun PlaceSearchScreen(
    modifier: Modifier = Modifier,
    onPlaceClicked: (String) -> Unit,
    viewModel: PlaceSearchViewModel = hiltViewModel(),
) {
    val listState = rememberLazyListState()
    val searchText by viewModel.searchText.collectAsStateWithLifecycle()
    val searchResult by viewModel.searchResult.collectAsStateWithLifecycle()

    listState.OnBottomReached {
        viewModel::loadNextPage.invoke()
    }

    OutlinedTextField(
        value = searchText,
        onValueChange = viewModel::onQueryChangedEvent
    )
//    PlacesList(
//        lazyListState = listState,
//        listState = searchResult,
//        onPlaceClicked = onPlaceClicked,
//        onRetryClicked = viewModel::onRetry,
//        modifier = modifier
//    )
}