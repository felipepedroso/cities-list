package br.pedroso.citieslist.features.citiessearch.fakes

import br.pedroso.citieslist.domain.entities.City
import br.pedroso.citieslist.domain.repository.CitiesRepository
import com.appmattus.kotlinfixture.kotlinFixture

class AlwaysSuccessfulFakeCitiesRepository : CitiesRepository {
    override suspend fun getCities(searchQuery: String): List<City> = CITIES_LIST

    companion object {
        val CITIES_LIST: List<City> = kotlinFixture().invoke()
    }
}