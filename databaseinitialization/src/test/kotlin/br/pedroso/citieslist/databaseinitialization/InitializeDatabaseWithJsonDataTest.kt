package br.pedroso.citieslist.databaseinitialization

import br.pedroso.citieslist.database.CitiesDao
import br.pedroso.citieslist.datasource.CitiesJsonDataSource
import br.pedroso.citieslist.datasource.JsonCity
import com.appmattus.kotlinfixture.decorator.nullability.NeverNullStrategy
import com.appmattus.kotlinfixture.decorator.nullability.nullabilityStrategy
import com.appmattus.kotlinfixture.kotlinFixture
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test

class InitializeDatabaseWithJsonDataTest {
    private val fixture =
        kotlinFixture {
            nullabilityStrategy(NeverNullStrategy)
        }

    private fun createUseCase(
        citiesDao: CitiesDao,
        citiesJsonDataSource: CitiesJsonDataSource,
    ) = InitializeDatabaseWithJsonData(citiesDao, citiesJsonDataSource)

    @Test
    fun `given data source is empty when use case is executed then database must be empty`() =
        runTest {
            val citiesDao = br.pedroso.citieslist.database.test.SuccessfulCitiesDao()
            val citiesJsonDataSource =
                br.pedroso.citieslist.datasource.test.FakeCitiesJsonDataSource(emptyList())
            val useCase = createUseCase(citiesDao, citiesJsonDataSource)

            useCase()

            assert(citiesDao.citiesInMemory.isEmpty())
        }

    @Test
    fun `given data source has cities when use case is executed then database must contain the same cities`() =
        runTest {
            val citiesDao = br.pedroso.citieslist.database.test.SuccessfulCitiesDao()
            val citiesJsonDataSource =
                br.pedroso.citieslist.datasource.test.FakeCitiesJsonDataSource(fixture<List<JsonCity>>())
            val expectedCities = citiesJsonDataSource.getCities().map(JsonCity::toDatabaseCity)
            val useCase = createUseCase(citiesDao, citiesJsonDataSource)

            useCase()

            assertThat(citiesDao.citiesInMemory).containsExactlyElementsIn(expectedCities)
        }
}