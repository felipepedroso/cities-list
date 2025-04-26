package br.pedroso.citieslist.features.starredcities

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import br.pedroso.citieslist.designsystem.components.EmptyState
import br.pedroso.citieslist.designsystem.components.PaginatedCitiesList
import br.pedroso.citieslist.designsystem.theme.CitiesListTheme
import br.pedroso.citieslist.designsystem.utils.createPreviewCities
import br.pedroso.citieslist.domain.City
import br.pedroso.citieslist.features.starredcities.StarredCitiesUiEvent.ClickedOnCity
import br.pedroso.citieslist.features.starredcities.StarredCitiesViewModelEvent.NavigateToMapScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun StarredCitiesScreen(
    viewModel: StarredCitiesViewModel,
    modifier: Modifier = Modifier,
    openCityOnMap: (city: City) -> Unit = {},
) {
    val lazyPagingItems = viewModel.paginatedCities.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.viewModelEventFlow.collectLatest { event ->
            if (event is NavigateToMapScreen) {
                openCityOnMap(event.cityToFocus)
            }
        }
    }

    StarredCitiesScreenUi(lazyPagingItems, modifier, viewModel::onUiEvent)
}

@Composable
private fun StarredCitiesScreenUi(
    lazyPagingItems: LazyPagingItems<City>,
    modifier: Modifier = Modifier,
    onUiEvent: (uiEvent: StarredCitiesUiEvent) -> Unit = {},
) {
    PaginatedCitiesList(
        modifier = modifier,
        lazyPagingItems = lazyPagingItems,
        showStarredIndicator = false,
        onCityClicked = { city -> onUiEvent(ClickedOnCity(city)) },
        headerContent = { itemsCount ->
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
            ) {
                Text(
                    text =
                        pluralStringResource(
                            id = R.plurals.starred_cities,
                            count = itemsCount,
                            itemsCount,
                        ),
                )
            }
        },
        errorStateContent = { },
        emptyStateContent = {
            EmptyState(
                message = stringResource(id = R.string.no_starred_cities),
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
fun StarredCitiesScreenPreview(
    @PreviewParameter(StarredCitiesScreenPagingDataProvider::class) pagingData: PagingData<City>,
) {
    CitiesListTheme {
        val pagingDataFlow = MutableStateFlow(pagingData)

        val lazyPagingItems = pagingDataFlow.collectAsLazyPagingItems()

        StarredCitiesScreenUi(lazyPagingItems)
    }
}

private class StarredCitiesScreenPagingDataProvider :
    PreviewParameterProvider<PagingData<City>> {
    override val values: Sequence<PagingData<City>> =
        sequenceOf(
            PagingData.from(createPreviewCities()),
            // Loading state
            PagingData.empty(
                LoadStates(
                    LoadState.Loading,
                    LoadState.NotLoading(true),
                    LoadState.NotLoading(true),
                ),
            ),
            // Empty state
            PagingData.empty(
                LoadStates(
                    LoadState.NotLoading(true),
                    LoadState.NotLoading(true),
                    LoadState.NotLoading(true),
                ),
            ),
            // Error state
            PagingData.empty(
                LoadStates(
                    LoadState.Error(Throwable()),
                    LoadState.NotLoading(true),
                    LoadState.NotLoading(true),
                ),
            ),
        )
}
