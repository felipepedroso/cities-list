package br.pedroso.citieslist.features.citiessearch

import androidx.paging.testing.asSnapshot
import br.pedroso.citieslist.domain.SearchQuery
import br.pedroso.citieslist.features.citiessearch.CitiesSearchUiEvent.ClickedOnCity
import br.pedroso.citieslist.features.citiessearch.CitiesSearchUiEvent.ClickedOnClearQuery
import br.pedroso.citieslist.features.citiessearch.CitiesSearchUiEvent.ClickedOnQueryHistoryItem
import br.pedroso.citieslist.features.citiessearch.CitiesSearchUiEvent.ClickedOnRemoveQueryHistoryItem
import br.pedroso.citieslist.features.citiessearch.CitiesSearchUiEvent.ClickedOnRetry
import br.pedroso.citieslist.features.citiessearch.CitiesSearchUiEvent.SeachQuerySubmitted
import br.pedroso.citieslist.features.citiessearch.CitiesSearchUiEvent.SearchQueryChanged
import br.pedroso.citieslist.features.citiessearch.CitiesSearchViewModelEvent.NavigateToMapScreen
import br.pedroso.citieslist.features.citiessearch.fakes.AlwaysEmptyFakeCitiesRepository
import br.pedroso.citieslist.features.citiessearch.fakes.AlwaysSuccessfulFakeCitiesRepository
import br.pedroso.citieslist.features.citiessearch.fakes.AlwaysThrowingExceptionFakeCitiesRepository
import br.pedroso.citieslist.features.citiessearch.fakes.FakeQueriesHistoryRepository
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
import java.util.Date

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
    fun `Given repository returns empty cities list, When screen is created, Then view must display empty state`() =
        runTest {
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysEmptyFakeCitiesRepository(),
                    FakeQueriesHistoryRepository(),
                )

            viewModel.onViewEvent(SearchQueryChanged(fixture()))

            val cities = viewModel.paginatedCities.asSnapshot()

            assertThat(cities).isEmpty()
        }

    @Test
    fun `Given repository throws exception, When screen is created, Then view must display error state`() =
        runTest {
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysThrowingExceptionFakeCitiesRepository(),
                    FakeQueriesHistoryRepository(),
                )

            viewModel.onViewEvent(SearchQueryChanged(fixture()))

            val error = runCatching { viewModel.paginatedCities.asSnapshot() }.exceptionOrNull()

            assertThat(error).isInstanceOf(AlwaysThrowingExceptionFakeCitiesRepository.EXCEPTION.javaClass)

            assertThat(error?.message).isEqualTo(AlwaysThrowingExceptionFakeCitiesRepository.EXCEPTION.message)
        }

    @Test
    fun `Given repository returns cities list, When screen is created, Then view must display same list`() =
        runTest {
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysSuccessfulFakeCitiesRepository(),
                    FakeQueriesHistoryRepository(),
                )

            viewModel.onViewEvent(SearchQueryChanged(fixture()))

            val cities = viewModel.paginatedCities.asSnapshot()

            assertThat(cities).isEqualTo(AlwaysSuccessfulFakeCitiesRepository.CITIES_LIST)
        }

    @Test
    fun `Given repository returns empty cities list, When search query changes, Then view must display empty state`() =
        runTest {
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysEmptyFakeCitiesRepository(),
                    FakeQueriesHistoryRepository(),
                )

            viewModel.onViewEvent(SearchQueryChanged(fixture()))

            val cities = viewModel.paginatedCities.asSnapshot()

            assertThat(cities).isEmpty()
        }

    @Test
    fun `Given repository throws exception, When search query changes, Then view must display error state`() =
        runTest {
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysThrowingExceptionFakeCitiesRepository(),
                    FakeQueriesHistoryRepository(),
                )

            viewModel.onViewEvent(SearchQueryChanged(fixture()))

            val error = runCatching { viewModel.paginatedCities.asSnapshot() }.exceptionOrNull()

            assertThat(error).isInstanceOf(AlwaysThrowingExceptionFakeCitiesRepository.EXCEPTION.javaClass)

            assertThat(error?.message).isEqualTo(AlwaysThrowingExceptionFakeCitiesRepository.EXCEPTION.message)
        }

    @Test
    fun `Given repository returns cities list, When search query changes, Then view must display same list`() =
        runTest {
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysSuccessfulFakeCitiesRepository(),
                    FakeQueriesHistoryRepository(),
                )

            viewModel.onViewEvent(SearchQueryChanged(fixture()))

            val cities = viewModel.paginatedCities.asSnapshot()

            assertThat(cities).isEqualTo(AlwaysSuccessfulFakeCitiesRepository.CITIES_LIST)
        }

    @Test
    fun `When user clicks on city, Then view must navigate to map screen`() =
        runTest {
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysSuccessfulFakeCitiesRepository(),
                    FakeQueriesHistoryRepository(),
                )

            val city: br.pedroso.citieslist.domain.City = fixture()

            viewModel.onViewEvent(ClickedOnCity(city))

            val event = viewModel.viewModelEventFlow.first() as NavigateToMapScreen

            assertThat(event.cityToFocus).isEqualTo(city)
        }

    @Test
    fun `Given repository returns empty cities list, When user clicks retry, Then view must display empty state`() =
        runTest {
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysEmptyFakeCitiesRepository(),
                    FakeQueriesHistoryRepository(),
                )

            viewModel.onViewEvent(ClickedOnRetry)

            val cities = viewModel.paginatedCities.asSnapshot()

            assertThat(cities).isEmpty()
        }

    @Test
    fun `Given repository throws exception, When user clicks retry, Then view must display error state`() =
        runTest {
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysThrowingExceptionFakeCitiesRepository(),
                    FakeQueriesHistoryRepository(),
                )

            viewModel.onViewEvent(ClickedOnRetry)

            val error = runCatching { viewModel.paginatedCities.asSnapshot() }.exceptionOrNull()

            assertThat(error).isInstanceOf(AlwaysThrowingExceptionFakeCitiesRepository.EXCEPTION.javaClass)

            assertThat(error?.message).isEqualTo(AlwaysThrowingExceptionFakeCitiesRepository.EXCEPTION.message)
        }

    @Test
    fun `Given repository returns cities list, When user clicks retry, Then view must display same list`() =
        runTest {
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysSuccessfulFakeCitiesRepository(),
                    FakeQueriesHistoryRepository(),
                )

            viewModel.onViewEvent(ClickedOnRetry)

            val cities = viewModel.paginatedCities.asSnapshot()

            assertThat(cities).isEqualTo(AlwaysSuccessfulFakeCitiesRepository.CITIES_LIST)
        }

    @Test
    fun `Given empty query, When user clicks retry, Then query state must remain empty`() =
        runTest {
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysSuccessfulFakeCitiesRepository(),
                    FakeQueriesHistoryRepository(),
                )

            viewModel.onViewEvent(ClickedOnRetry)

            assertThat(viewModel.uiQueryState.first()).isEmpty()
        }

    @Test
    fun `Given non-empty query, When user clicks retry, Then query state must remain unchanged`() =
        runTest {
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysSuccessfulFakeCitiesRepository(),
                    FakeQueriesHistoryRepository(),
                )

            val query: String = fixture()

            viewModel.onViewEvent(SearchQueryChanged(query))

            viewModel.onViewEvent(ClickedOnRetry)

            assertThat(viewModel.uiQueryState.first()).isEqualTo(query)
        }

    @Test
    fun `When user types new query, Then query state must be updated`() =
        runTest {
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysSuccessfulFakeCitiesRepository(),
                    FakeQueriesHistoryRepository(),
                )

            val newQuery: String = fixture()

            viewModel.onViewEvent(SearchQueryChanged(newQuery))

            assertThat(viewModel.uiQueryState.first()).isEqualTo(newQuery)
        }

    @Test
    fun `Given non-empty query, When user clicks clear button, Then query state must be empty`() =
        runTest {
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysSuccessfulFakeCitiesRepository(),
                    FakeQueriesHistoryRepository(),
                )

            val newQuery: String = fixture()

            viewModel.onViewEvent(SearchQueryChanged(newQuery))

            viewModel.onViewEvent(ClickedOnClearQuery)

            assertThat(viewModel.uiQueryState.first()).isEmpty()
        }

    @Test
    fun `When user submits query, Then it must be added to history`() =
        runTest {
            val queriesHistoryRepository = FakeQueriesHistoryRepository()
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysSuccessfulFakeCitiesRepository(),
                    queriesHistoryRepository,
                )

            val query = "test query"
            viewModel.onViewEvent(SearchQueryChanged(query))
            viewModel.onViewEvent(SeachQuerySubmitted)

            testCoroutineDispatcher.scheduler.advanceUntilIdle()

            val queries = queriesHistoryRepository.getAllQueries().first()
            assertThat(queries.map { it.query }).contains(query)
        }

    @Test
    fun `When user clicks history item, Then query state must be updated`() =
        runTest {
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysSuccessfulFakeCitiesRepository(),
                    FakeQueriesHistoryRepository(),
                )

            val historyQuery =
                SearchQuery(
                    id = 1,
                    query = "history query",
                    timestamp = Date(),
                )
            viewModel.onViewEvent(ClickedOnQueryHistoryItem(historyQuery))

            testCoroutineDispatcher.scheduler.advanceUntilIdle()

            assertThat(viewModel.uiQueryState.first()).isEqualTo(historyQuery.query)
        }

    @Test
    fun `When user removes history item, Then it must be removed from history`() =
        runTest {
            val queriesHistoryRepository = FakeQueriesHistoryRepository()
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysSuccessfulFakeCitiesRepository(),
                    queriesHistoryRepository,
                )

            val query = "history query"
            viewModel.onViewEvent(SearchQueryChanged(query))
            viewModel.onViewEvent(SeachQuerySubmitted)
            testCoroutineDispatcher.scheduler.advanceUntilIdle()

            val historyQuery = queriesHistoryRepository.getAllQueries().first().first()

            viewModel.onViewEvent(ClickedOnRemoveQueryHistoryItem(historyQuery))
            testCoroutineDispatcher.scheduler.advanceUntilIdle()

            // Verify it was removed
            val queries = queriesHistoryRepository.getAllQueries().first()
            assertThat(queries).isEmpty()
        }

    @Test
    fun `Given query with different case, When searching, Then it must be treated as same query`() =
        runTest {
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysSuccessfulFakeCitiesRepository(),
                    FakeQueriesHistoryRepository(),
                )

            val query1 = "TEST"
            val query2 = "test"

            viewModel.onViewEvent(SearchQueryChanged(query1))
            val cities1 = viewModel.paginatedCities.asSnapshot()

            viewModel.onViewEvent(SearchQueryChanged(query2))
            val cities2 = viewModel.paginatedCities.asSnapshot()

            assertThat(cities1).isEqualTo(cities2)
        }

    @Test
    fun `Given submitted query, When retrying, Then query must persist`() =
        runTest {
            val viewModel =
                CitiesSearchViewModel(
                    AlwaysSuccessfulFakeCitiesRepository(),
                    FakeQueriesHistoryRepository(),
                )

            val query = "test query"
            viewModel.onViewEvent(SearchQueryChanged(query))
            viewModel.onViewEvent(SeachQuerySubmitted)
            viewModel.onViewEvent(ClickedOnRetry)

            assertThat(viewModel.uiQueryState.first()).isEqualTo(query)
        }
}
