package br.pedroso.citieslist.features.citiessearch

import androidx.paging.testing.asSnapshot
import br.pedroso.citieslist.features.citiessearch.CitiesSearchUiEvent.ClickedOnCity
import br.pedroso.citieslist.features.citiessearch.CitiesSearchUiEvent.ClickedOnClearQuery
import br.pedroso.citieslist.features.citiessearch.CitiesSearchUiEvent.ClickedOnRetry
import br.pedroso.citieslist.features.citiessearch.CitiesSearchUiEvent.SearchQueryChanged
import br.pedroso.citieslist.features.citiessearch.CitiesSearchViewModelEvent.NavigateToMapScreen
import br.pedroso.citieslist.features.citiessearch.fakes.AlwaysEmptyFakeCitiesRepository
import br.pedroso.citieslist.features.citiessearch.fakes.AlwaysSuccessfulFakeCitiesRepository
import br.pedroso.citieslist.features.citiessearch.fakes.AlwaysThrowingExceptionFakeCitiesRepository
import com.appmattus.kotlinfixture.kotlinFixture
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CitiesSearchViewModelTest {
    private val fixture = kotlinFixture()

    private var testCoroutineDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        testCoroutineDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given repository returns an empty list of cities when screen is created then view must display the empty state`() =
        runTest {
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysEmptyFakeCitiesRepository(),
                )

            viewModel.onViewEvent(SearchQueryChanged(fixture()))

            val cities = viewModel.paginatedCities.asSnapshot()

            assertThat(cities).isEmpty()
        }

    @Test
    fun `given repository throws an exception when screen is created then view must display the error state`() =
        runTest {
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysThrowingExceptionFakeCitiesRepository(),
                )

            viewModel.onViewEvent(SearchQueryChanged(fixture()))

            val error = runCatching { viewModel.paginatedCities.asSnapshot() }.exceptionOrNull()

            assertThat(error).isInstanceOf(AlwaysThrowingExceptionFakeCitiesRepository.EXCEPTION.javaClass)

            assertThat(error?.message).isEqualTo(AlwaysThrowingExceptionFakeCitiesRepository.EXCEPTION.message)
        }

    @Test
    fun `given repository returns a list of cities when screen is created then view must display the same list`() =
        runTest {
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysSuccessfulFakeCitiesRepository(),
                )

            viewModel.onViewEvent(SearchQueryChanged(fixture()))

            val cities = viewModel.paginatedCities.asSnapshot()

            assertThat(cities).isEqualTo(AlwaysSuccessfulFakeCitiesRepository.CITIES_LIST)
        }

    @Test
    fun `given repository returns an empty list of cities when the search query changes then view must display the empty state`() =
        runTest {
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysEmptyFakeCitiesRepository(),
                )

            viewModel.onViewEvent(SearchQueryChanged(fixture()))

            val cities = viewModel.paginatedCities.asSnapshot()

            assertThat(cities).isEmpty()
        }

    @Test
    fun `given repository throws an exception when the search query changes then view must display the error state`() =
        runTest {
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysThrowingExceptionFakeCitiesRepository(),
                )

            viewModel.onViewEvent(SearchQueryChanged(fixture()))

            val error = runCatching { viewModel.paginatedCities.asSnapshot() }.exceptionOrNull()

            assertThat(error).isInstanceOf(AlwaysThrowingExceptionFakeCitiesRepository.EXCEPTION.javaClass)

            assertThat(error?.message).isEqualTo(AlwaysThrowingExceptionFakeCitiesRepository.EXCEPTION.message)
        }

    @Test
    fun `given repository returns a list of cities when the search query changes then view must display the same list`() =
        runTest {
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysSuccessfulFakeCitiesRepository(),
                )

            viewModel.onViewEvent(SearchQueryChanged(fixture()))

            val cities = viewModel.paginatedCities.asSnapshot()

            assertThat(cities).isEqualTo(AlwaysSuccessfulFakeCitiesRepository.CITIES_LIST)
        }

    @Test
    fun `when user clicks on city then view must navigate to maps screen`() =
        runTest {
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysSuccessfulFakeCitiesRepository(),
                )

            val city: br.pedroso.citieslist.domain.City = fixture()

            viewModel.onViewEvent(ClickedOnCity(city))

            val event = viewModel.viewModelEventFlow.first() as NavigateToMapScreen

            assertThat(event.cityToFocus).isEqualTo(city)
        }

    @Test
    fun `given repository returns an empty list of cities when user clicked on retry then view must display the empty state`() =
        runTest {
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysEmptyFakeCitiesRepository(),
                )

            viewModel.onViewEvent(ClickedOnRetry)

            val cities = viewModel.paginatedCities.asSnapshot()

            assertThat(cities).isEmpty()
        }

    @Test
    fun `given repository throws an exception when user clicked on retry then view must display the error state`() =
        runTest {
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysThrowingExceptionFakeCitiesRepository(),
                )

            viewModel.onViewEvent(ClickedOnRetry)

            val error = runCatching { viewModel.paginatedCities.asSnapshot() }.exceptionOrNull()

            assertThat(error).isInstanceOf(AlwaysThrowingExceptionFakeCitiesRepository.EXCEPTION.javaClass)

            assertThat(error?.message).isEqualTo(AlwaysThrowingExceptionFakeCitiesRepository.EXCEPTION.message)
        }

    @Test
    fun `given repository returns a list of cities when user clicked on retry then view must display the same list`() =
        runTest {
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysSuccessfulFakeCitiesRepository(),
                )

            viewModel.onViewEvent(ClickedOnRetry)

            val cities = viewModel.paginatedCities.asSnapshot()

            assertThat(cities).isEqualTo(AlwaysSuccessfulFakeCitiesRepository.CITIES_LIST)
        }

    @Test
    fun `given the query is empty when user clicks on retry then query state must also be empty`() =
        runTest {
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysSuccessfulFakeCitiesRepository(),
                )

            viewModel.onViewEvent(ClickedOnRetry)

            assertThat(viewModel.uiQueryState.first()).isEmpty()
        }

    @Test
    fun `given the query is not empty when user clicks on retry then query state must remain the same`() =
        runTest {
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysSuccessfulFakeCitiesRepository(),
                )

            val query: String = fixture()

            viewModel.onViewEvent(SearchQueryChanged(query))

            viewModel.onViewEvent(ClickedOnRetry)

            assertThat(viewModel.uiQueryState.first()).isEqualTo(query)
        }

    @Test
    fun `when user types a new query then query state must be updated`() =
        runTest {
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysSuccessfulFakeCitiesRepository(),
                )

            val newQuery: String = fixture()

            viewModel.onViewEvent(SearchQueryChanged(newQuery))

            assertThat(viewModel.uiQueryState.first()).isEqualTo(newQuery)
        }

    @Test
    fun `given the query is not empty when user clicks on the clear query button then query state must be empty`() =
        runTest {
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysSuccessfulFakeCitiesRepository(),
                )

            val newQuery: String = fixture()

            viewModel.onViewEvent(SearchQueryChanged(newQuery))

            viewModel.onViewEvent(ClickedOnClearQuery)

            assertThat(viewModel.uiQueryState.first()).isEmpty()
        }
}
