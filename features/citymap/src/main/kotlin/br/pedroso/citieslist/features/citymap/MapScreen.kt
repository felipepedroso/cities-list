package br.pedroso.citieslist.features.citymap

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import br.pedroso.citieslist.designsystem.components.ErrorState
import br.pedroso.citieslist.designsystem.components.LoadingState
import br.pedroso.citieslist.designsystem.theme.CitiesListTheme
import br.pedroso.citieslist.features.citymap.MapScreenUiState.DisplayCity
import br.pedroso.citieslist.features.citymap.MapScreenUiState.Error
import br.pedroso.citieslist.features.citymap.MapScreenUiState.Loading
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import br.pedroso.citieslist.designsystem.R as DesignSystemR

@Composable
fun MapScreen(
    viewModel: MapScreenViewModel,
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    MapScreenUi(
        uiState = uiState,
        modifier = modifier,
        onNavigateUp = onNavigateUp,
        updateBookmarkState = viewModel::updateStarredState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreenUi(
    uiState: MapScreenUiState,
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit = {},
    updateBookmarkState: (city: br.pedroso.citieslist.domain.City, newBookmarkState: Boolean) -> Unit = { _, _ -> },
) {
    val title: String =
        when (uiState) {
            is DisplayCity -> with(uiState.city) { "$name, $countryCode" }
            is Error -> stringResource(id = R.string.error_title)
            Loading -> ""
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = title) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            painter = painterResource(id = DesignSystemR.drawable.ic_arrow_back),
                            contentDescription = null,
                        )
                    }
                },
                actions = {
                    if (uiState is DisplayCity) {
                        val city = uiState.city
                        val isBookmarked = city.isStarred
                        IconButton(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            onClick = { updateBookmarkState(city, !isBookmarked) },
                        ) {
                            Icon(
                                painter =
                                    painterResource(
                                        if (isBookmarked) {
                                            R.drawable.ic_star_filled
                                        } else {
                                            R.drawable.ic_star_outlined
                                        },
                                    ),
                                contentDescription = null,
                            )
                        }
                    }
                },
            )
        },
    ) { paddingValues ->
        AnimatedContent(
            modifier = modifier.padding(paddingValues),
            targetState = uiState,
            label = "ui-state-animation",
            contentKey = { (it as? DisplayCity)?.city?.id },
        ) { state ->
            val stateModifier = Modifier.fillMaxSize()

            when (state) {
                is DisplayCity -> DisplayCityOnMap(modifier = stateModifier, city = state.city)
                is Error ->
                    ErrorState(
                        message = stringResource(id = R.string.generic_error),
                        buttonText = stringResource(id = R.string.go_back),
                        onButtonClick = onNavigateUp,
                    )

                Loading -> LoadingState(modifier = stateModifier)
            }
        }
    }
}

@Composable
fun DisplayCityOnMap(
    city: br.pedroso.citieslist.domain.City,
    modifier: Modifier = Modifier,
) {
    val cityLatLng = LatLng(city.coordinates.latitude, city.coordinates.longitude)

    val cameraPositionState =
        rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(cityLatLng, 10f)
        }

    GoogleMap(modifier = modifier, cameraPositionState = cameraPositionState) {
        Marker(
            state = MarkerState(position = cityLatLng),
            title = city.name,
            snippet = "Marker in ${city.name}",
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun MapScreenPreview(
    @PreviewParameter(MapScreenCityPreviewParameterProvider::class) uiState: MapScreenUiState,
) {
    CitiesListTheme {
        MapScreenUi(
            modifier = Modifier.fillMaxSize(),
            uiState = uiState,
        )
    }
}

private class MapScreenCityPreviewParameterProvider : PreviewParameterProvider<MapScreenUiState> {
    override val values: Sequence<MapScreenUiState> =
        sequenceOf(
            DisplayCity(
                city =
                    br.pedroso.citieslist.domain.City(
                        name = "Bristol",
                        countryCode = "GB",
                        coordinates = br.pedroso.citieslist.domain.Coordinates(51.4552, -2.5967),
                        id = 1,
                        isStarred = true,
                    ),
            ),
            Loading,
            Error(Throwable()),
        )
}
