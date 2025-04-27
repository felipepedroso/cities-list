package br.pedroso.citieslist.features.citiessearch.fakes

import br.pedroso.citieslist.domain.SearchQuery
import br.pedroso.citieslist.queryhistory.QueriesHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Date

class FakeQueriesHistoryRepository : QueriesHistoryRepository {
    private val queries = MutableStateFlow(emptyList<SearchQuery>())
    private var nextId = 1

    override fun getAllQueries(): Flow<List<SearchQuery>> = queries

    override suspend fun insertQuery(query: String) {
        val newQuery =
            SearchQuery(
                id = nextId++,
                query = query,
                timestamp = Date(),
            )
        queries.value += newQuery
    }

    override suspend fun removeQuery(searchQuery: SearchQuery) {
        queries.value = queries.value.filter { it.id != searchQuery.id }
    }
} 
