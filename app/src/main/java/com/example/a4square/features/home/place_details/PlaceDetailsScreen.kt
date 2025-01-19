package com.example.a4square.features.home.place_details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PlaceDetailsScreen(viewModel: PlaceDetailsViewModel = hiltViewModel()) {
//    val moviesState by viewModel.stateObservable.collectAsStateWithLifecycle()
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { }) {
            Text("PlaceDetails")
        }
    }
}