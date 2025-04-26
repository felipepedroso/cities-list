package br.pedroso.citieslist.features.starredcities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import br.pedroso.citieslist.domain.City
import br.pedroso.citieslist.features.starredcities.StarredCitiesUiEvent.ClickedOnCity
import br.pedroso.citieslist.features.starredcities.StarredCitiesUiEvent.ClickedOnRetry
import br.pedroso.citieslist.features.starredcities.StarredCitiesViewModelEvent.NavigateToMapScreen
import br.pedroso.citieslist.repository.CitiesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StarredCitiesViewModel
    @Inject
    constructor(citiesRepository: CitiesRepository) : ViewModel() {
        val paginatedCities: Flow<PagingData<City>> =
            citiesRepository.getStarredCities().cachedIn(viewModelScope)

        private val viewModelEventChannel = Channel<StarredCitiesViewModelEvent>(Channel.BUFFERED)
        val viewModelEventFlow: Flow<StarredCitiesViewModelEvent>
            get() = viewModelEventChannel.receiveAsFlow()

        fun onUiEvent(viewEvent: StarredCitiesUiEvent) {
            when (viewEvent) {
                is ClickedOnCity ->
                    viewModelScope.launch {
                        viewModelEventChannel.send(NavigateToMapScreen(viewEvent.city))
                    }

                ClickedOnRetry -> TODO()
            }
        }
    }
