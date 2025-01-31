package com.example.a4square.features.home.place_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.a4square.R

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PlaceDetailsScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    viewModel: PlaceDetailsViewModel = hiltViewModel(),
) {
    val screenState by viewModel.placeDetails.collectAsStateWithLifecycle()

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center)
                    { Text("Marko Kostic", textAlign = TextAlign.Center) }
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                })
        },
    ) { innerPadding ->
        Column(
            modifier
                .fillMaxWidth()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        when (val placeDetails = screenState) {
            is PlaceDetailsState.PlaceDetailsFailed -> Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = dimensionResource(id = R.dimen.spacing_2x))
            ) {
                Text("Fetch failed")
                Button(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(all = 16.dp),
                    onClick = viewModel::onRetry
                ) {
                    Text(text = "Retry")
                }
            }

            PlaceDetailsState.PlaceDetailsLoading -> Text("Loading")
            is PlaceDetailsState.PlaceDetailsSuccess -> {
                if (placeDetails.placeDetails == null) Text("Not found in cache")
                else Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(horizontal = dimensionResource(id = R.dimen.spacing_2x))
                ) {
                    GlideImage(
                        model = placeDetails.placeDetails.iconUrl,
                        contentDescription = "icon",
                        modifier = modifier
                            .aspectRatio(ratio = 1f)
                            .fillMaxWidth(),
                    ) {
                        it.error(R.drawable.error).placeholder(R.drawable.image)
                            .load(placeDetails.placeDetails.iconUrl)
                    }
                    Row(
                        modifier = modifier.padding(all = dimensionResource(id = R.dimen.spacing_1x)),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            modifier = modifier.weight(1f),
                            text = placeDetails.placeDetails.name,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(
                            onClick = { viewModel.onChangePlaceFavouriteStatus(placeId = placeDetails.placeDetails.id) },
                            modifier
                        ) {
                            Icon(
                                if (placeDetails.placeDetails.isFavourite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                                "Favourite"
                            )
                        }
                    }
                    Text(
                        modifier = modifier.fillMaxWidth(),
                        text = placeDetails.placeDetails.id,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        modifier = modifier.fillMaxWidth(),
                        text = placeDetails.placeDetails.timeZone ?: "no time zone available",
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        modifier = modifier.fillMaxWidth(),
                        text = placeDetails.placeDetails.link ?: "no link available",
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }

        }
    }
    }
}
