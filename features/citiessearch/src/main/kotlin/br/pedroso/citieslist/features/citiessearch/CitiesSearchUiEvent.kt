package br.pedroso.citieslist.features.citiessearch

import br.pedroso.citieslist.domain.City
import br.pedroso.citieslist.domain.SearchQuery

sealed class CitiesSearchUiEvent {
    class SearchQueryChanged(val newQuery: String) : CitiesSearchUiEvent()

    data object SeachQuerySubmitted : CitiesSearchUiEvent()

    class ClickedOnCity(val city: City) : CitiesSearchUiEvent()

    class ClickedOnQueryHistoryItem(val searchQuery: SearchQuery) : CitiesSearchUiEvent()

    class ClickedOnRemoveQueryHistoryItem(val searchQuery: SearchQuery) : CitiesSearchUiEvent()

    data object ClickedOnRetry : CitiesSearchUiEvent()

    data object ClickedOnClearQuery : CitiesSearchUiEvent()
}
