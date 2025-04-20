package br.pedroso.citieslist.features.citiessearch

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
import br.pedroso.citieslist.designsystem.components.BaseScreen
import br.pedroso.citieslist.designsystem.components.EmptyState
import br.pedroso.citieslist.designsystem.components.ErrorState
import br.pedroso.citieslist.designsystem.components.PaginatedCitiesList
import br.pedroso.citieslist.designsystem.theme.CitiesListTheme
import br.pedroso.citieslist.designsystem.utils.createPreviewCities
import br.pedroso.citieslist.domain.City
import br.pedroso.citieslist.features.citiessearch.CitiesSearchUiEvent.ClickedOnCity
import br.pedroso.citieslist.features.citiessearch.CitiesSearchUiEvent.ClickedOnClearQuery
import br.pedroso.citieslist.features.citiessearch.CitiesSearchUiEvent.ClickedOnRetry
import br.pedroso.citieslist.features.citiessearch.CitiesSearchUiEvent.SearchQueryChanged
import br.pedroso.citieslist.features.citiessearch.CitiesSearchViewModelEvent.NavigateToMapScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import br.pedroso.citieslist.designsystem.R as DesignSystemR

@Composable
fun CitiesSearchScreen(
    viewModel: CitiesSearchViewModel,
    modifier: Modifier = Modifier,
    openCityOnMap: (city: City) -> Unit = {},
) {
    val lazyPagingItems = viewModel.paginatedCities.collectAsLazyPagingItems()

    val query by viewModel.queryState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.viewModelEventFlow.collectLatest { event ->
            if (event is NavigateToMapScreen) {
                openCityOnMap(event.cityToFocus)
            }
        }
    }

    CitiesSearchScreenUi(
        modifier = modifier,
        query = query,
        lazyPagingItems = lazyPagingItems,
        onViewEvent = viewModel::onViewEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitiesSearchScreenUi(
    query: String,
    lazyPagingItems: LazyPagingItems<City>,
    modifier: Modifier = Modifier,
    onViewEvent: (viewEvent: CitiesSearchUiEvent) -> Unit = {},
) {
    val isLoading = lazyPagingItems.loadState.refresh is LoadState.Loading
    var expanded by rememberSaveable { mutableStateOf(false) }

    val searchBarHorizontalPadding by animateDpAsState(
        if (expanded) {
            0.dp
        } else {
            16.dp
        },
    )

    BaseScreen(modifier) {
        Column(modifier = modifier.fillMaxSize()) {
            SearchBar(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(searchBarHorizontalPadding),
                inputField = {
                    SearchBarDefaults.InputField(
                        query = query,
                        onQueryChange = { newQuery -> onViewEvent(SearchQueryChanged(newQuery)) },
                        onSearch = { expanded = false },
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        placeholder = { Text(text = stringResource(id = R.string.search_placeholder)) },
                        trailingIcon = {
                            if (!isLoading && query.isNotEmpty()) {
                                IconButton(onClick = { onViewEvent(ClickedOnClearQuery) }) {
                                    Icon(
                                        painter = painterResource(id = DesignSystemR.drawable.ic_close),
                                        contentDescription = null,
                                    )
                                }
                            }
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = DesignSystemR.drawable.ic_search),
                                contentDescription = null,
                            )
                        },
                    )
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
            ) {
            }

            PaginatedCitiesList(
                lazyPagingItems = lazyPagingItems,
                onCityClicked = { city -> onViewEvent(ClickedOnCity(city)) },
                headerContent = { itemsCount ->
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                    ) {
                        Text(
                            text =
                                pluralStringResource(
                                    id =
                                        if (query.isEmpty()) {
                                            R.plurals.query_empty
                                        } else {
                                            R.plurals.query_result
                                        },
                                    count = itemsCount,
                                    itemsCount,
                                ),
                        )
                    }
                },
                errorStateContent = {
                    ErrorState(
                        message = stringResource(id = R.string.generic_error),
                        buttonText = stringResource(id = R.string.retry),
                        onButtonClick = { onViewEvent(ClickedOnRetry) },
                    )
                },
                emptyStateContent = {
                    EmptyState(
                        message = stringResource(id = R.string.empty_list),
                    )
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun CitiesSearchScreenPreview(
    @PreviewParameter(CitiesSearchScreenPagingDataProvider::class) pagingData: PagingData<br.pedroso.citieslist.domain.City>,
) {
    val pagingDataFlow = MutableStateFlow(pagingData)

    val lazyPagingItems = pagingDataFlow.collectAsLazyPagingItems()

    CitiesListTheme {
        CitiesSearchScreenUi(
            query = PREVIEW_QUERY,
            lazyPagingItems = lazyPagingItems,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

private const val PREVIEW_QUERY = "City"

private class CitiesSearchScreenPagingDataProvider :
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
