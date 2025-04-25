package br.pedroso.citieslist.features.citiessearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import br.pedroso.citieslist.domain.City
import br.pedroso.citieslist.domain.SearchQuery
import br.pedroso.citieslist.features.citiessearch.CitiesSearchUiEvent.ClickedOnCity
import br.pedroso.citieslist.features.citiessearch.CitiesSearchUiEvent.ClickedOnClearQuery
import br.pedroso.citieslist.features.citiessearch.CitiesSearchUiEvent.ClickedOnQueryHistoryItem
import br.pedroso.citieslist.features.citiessearch.CitiesSearchUiEvent.ClickedOnRemoveQueryHistoryItem
import br.pedroso.citieslist.features.citiessearch.CitiesSearchUiEvent.ClickedOnRetry
import br.pedroso.citieslist.features.citiessearch.CitiesSearchUiEvent.SeachQuerySubmitted
import br.pedroso.citieslist.features.citiessearch.CitiesSearchUiEvent.SearchQueryChanged
import br.pedroso.citieslist.features.citiessearch.CitiesSearchViewModelEvent.NavigateToMapScreen
import br.pedroso.citieslist.queryhistory.QueriesHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CitiesSearchViewModel
    @Inject
    constructor(
        private val citiesRepository: br.pedroso.citieslist.repository.CitiesRepository,
        private val queriesHistoryRepository: QueriesHistoryRepository,
    ) : ViewModel() {
        private val _uiQueryState: MutableStateFlow<String> = MutableStateFlow("")

        val uiQueryState: StateFlow<String> = _uiQueryState

        private val paginatedListQuery: MutableStateFlow<String> = MutableStateFlow("")

        val paginatedCities: Flow<PagingData<City>> =
            paginatedListQuery
                .distinctUntilChanged { old, new -> old.compareTo(new, ignoreCase = true) == 0 }
                .flatMapLatest { citiesRepository.getCities(it) }
                .cachedIn(viewModelScope)

        val queriesHistoryState: StateFlow<List<SearchQuery>> =
            queriesHistoryRepository.getAllQueries()
                .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

        private val viewModelEventChannel = Channel<CitiesSearchViewModelEvent>(Channel.BUFFERED)

        val viewModelEventFlow: Flow<CitiesSearchViewModelEvent>
            get() = viewModelEventChannel.receiveAsFlow()

        fun onViewEvent(viewEvent: CitiesSearchUiEvent) {
            when (viewEvent) {
                is ClickedOnCity ->
                    viewModelScope.launch {
                        viewModelEventChannel.send(NavigateToMapScreen(viewEvent.city))
                    }

                is SearchQueryChanged -> _uiQueryState.update { viewEvent.newQuery }
                ClickedOnRetry -> _uiQueryState.update { it }
                ClickedOnClearQuery -> {
                    _uiQueryState.update { "" }
                    paginatedListQuery.update { "" }
                }
                SeachQuerySubmitted -> submitQuery()
                is ClickedOnQueryHistoryItem -> clickedOnQueryHistoryItem(viewEvent.searchQuery)
                is ClickedOnRemoveQueryHistoryItem -> removeQueryHistoryItem(viewEvent.searchQuery)
            }
        }

        private fun removeQueryHistoryItem(searchQuery: SearchQuery) {
            viewModelScope.launch {
                queriesHistoryRepository.removeQuery(searchQuery)
            }
        }

        private fun clickedOnQueryHistoryItem(searchQuery: SearchQuery) {
            _uiQueryState.update { searchQuery.query }
            paginatedListQuery.update { searchQuery.query }
        }

        private fun submitQuery() {
            val query = _uiQueryState.value

            viewModelScope.launch {
                queriesHistoryRepository.insertQuery(query)
            }

            paginatedListQuery.update { _uiQueryState.value }
        }
    }
