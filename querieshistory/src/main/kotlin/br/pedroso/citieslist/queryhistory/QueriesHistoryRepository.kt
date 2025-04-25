package br.pedroso.citieslist.queryhistory

import br.pedroso.citieslist.domain.SearchQuery
import kotlinx.coroutines.flow.Flow

interface QueriesHistoryRepository {
    fun getAllQueries(): Flow<List<SearchQuery>>

    suspend fun insertQuery(query: String)

    suspend fun removeQuery(searchQuery: SearchQuery)
}
