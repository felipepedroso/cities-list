package br.pedroso.citieslist.databaseinitialization

import br.pedroso.citieslist.database.CitiesDao
import br.pedroso.citieslist.database.test.SuccessfulCitiesDao
import br.pedroso.citieslist.datasource.CitiesJsonDataSource
import br.pedroso.citieslist.datasource.JsonCity
import br.pedroso.citieslist.datasource.test.FakeCitiesJsonDataSource
import com.appmattus.kotlinfixture.decorator.nullability.NeverNullStrategy
import com.appmattus.kotlinfixture.decorator.nullability.nullabilityStrategy
import com.appmattus.kotlinfixture.kotlinFixture
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test

class InitializeDatabaseUseCaseTest {
    private val fixture =
        kotlinFixture {
            nullabilityStrategy(NeverNullStrategy)
        }

    private fun createUseCase(
        citiesDao: CitiesDao,
        citiesJsonDataSource: CitiesJsonDataSource,
    ) = InitializeDatabaseUseCaseImpl(citiesDao, citiesJsonDataSource)

    @Test
    fun `Given data source is empty, When use case is executed, Then database must be empty`() =
        runTest {
            val citiesDao = SuccessfulCitiesDao()
            val citiesJsonDataSource = FakeCitiesJsonDataSource(emptyList())
            val useCase = createUseCase(citiesDao, citiesJsonDataSource)

            useCase()

            assert(citiesDao.citiesInMemory.isEmpty())
        }

    @Test
    fun `given data source has cities when use case is executed then database must contain the same cities`() =
        runTest {
            val citiesDao = SuccessfulCitiesDao()
            val citiesJsonDataSource = FakeCitiesJsonDataSource(fixture<List<JsonCity>>())
            val expectedCities = citiesJsonDataSource.getCities().map(JsonCity::toDatabaseCity)
            val useCase = createUseCase(citiesDao, citiesJsonDataSource)

            useCase()

            assertThat(citiesDao.citiesInMemory).containsExactlyElementsIn(expectedCities)
        }
}
