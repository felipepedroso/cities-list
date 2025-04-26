package br.pedroso.citieslist.features.starredcities

import br.pedroso.citieslist.domain.City

sealed class StarredCitiesUiEvent {
    class ClickedOnCity(val city: City) : StarredCitiesUiEvent()

    data object ClickedOnRetry : StarredCitiesUiEvent()
}
