package com.example.a4square.features.home.places_search.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.a4square.R
import com.example.a4square.features.home.places_search.PlacesListState
import com.example.a4square.features.home.places_search.hasNextPage
import com.example.domain.entity.Place

@Composable
fun PlacesList(
    lazyListState: LazyListState,
    modifier: Modifier = Modifier,
    listState: PlacesListState,
    onPlaceClicked: (String) -> Unit,
    onRetryClicked: () -> Unit,
) {

    LazyColumn(
        state = lazyListState, modifier = modifier.fillMaxSize()
    ) {
        items(
            listState.places.size + if (listState.hasNextPage) 1 else 0,
            key = { index -> listState.places[index].id },
            itemContent = { index ->
                when (listState) {
                    is PlacesListState.PlacesListStateEndOfList -> {
                        PlaceListItem(
                            place = listState.places[index],
                            onPlaceClicked = onPlaceClicked,
                            modifier = modifier
                        )
                    }

                    is PlacesListState.PlacesListStateFailed -> {
                        if (index < listState.places.size) {
                            PlaceListItem(
                                place = listState.places[index],
                                onPlaceClicked = onPlaceClicked,
                                modifier = modifier
                            )
                        } else {
                            PlaceListErrorItem(
                                onRetryClicked = onRetryClicked,
                                modifier = modifier
                            )
                        }
                    }

                    is PlacesListState.PlacesListStateLoaded,
                    is PlacesListState.PlacesListStateLoading -> {
                        if (index < listState.places.size) {
                            PlaceListItem(
                                place = listState.places[index],
                                onPlaceClicked = onPlaceClicked,
                                modifier = modifier
                            )
                        } else {
                            PlaceListLoadingItem(modifier = modifier)
                        }
                    }

                    is PlacesListState.PlacesListStateIdle -> PlaceListInfoItem(modifier = modifier)
                }
            })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceListItem(
    place: Place, onPlaceClicked: (String) -> Unit, modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .padding(
                vertical = dimensionResource(id = R.dimen.spacing_1x),
                horizontal = dimensionResource(id = R.dimen.spacing_2x)
            )
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.spacing_12x)),
        onClick = {
            onPlaceClicked(place.id)
        },
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Text(
            modifier = modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            text = place.name,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
@Preview
fun PlaceListInfoItem(modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Text(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
            text = "Type 3 or more chars...",
            maxLines = 1,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        LinearProgressIndicator(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
        )
    }
}

@Composable
@Preview
fun PlaceListLoadingItem(modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Text(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
            text = "Loading",
            maxLines = 1,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        LinearProgressIndicator(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
        )
    }
}

@Composable
fun PlaceListErrorItem(
    onRetryClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Column(modifier = modifier) {
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
                text = "Something went wrong...",
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Button(modifier = modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
                onClick = { onRetryClicked() }) {
                Text(text = "Retry")
            }
        }
    }
}

@Composable
fun LazyListState.OnBottomReached(
    onBottomReached: () -> Unit
) {

    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem =
                layoutInfo.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf false

            lastVisibleItem.index > layoutInfo.totalItemsCount - 6
        }
    }

    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }.collect {
            // if should load more, then invoke loadMore
            if (it) {
                onBottomReached()
            }
        }
    }
}

//@Immutable za presentation model tipa uiItem