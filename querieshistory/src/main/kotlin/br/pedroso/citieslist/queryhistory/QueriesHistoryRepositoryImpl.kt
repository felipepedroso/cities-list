package br.pedroso.citieslist.queryhistory

import br.pedroso.citieslist.database.DatabaseSearchQuery
import br.pedroso.citieslist.database.QueriesDao
import br.pedroso.citieslist.domain.SearchQuery
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
import javax.inject.Named

class QueriesHistoryRepositoryImpl
    @Inject
    constructor(
        private val queriesDao: QueriesDao,
        @Named("IoDispatcher") private val ioDispatcher: CoroutineDispatcher,
    ) : QueriesHistoryRepository {
        override fun getAllQueries(): Flow<List<SearchQuery>> = queriesDao.getAllQueries().map { queries -> queries.map { it.toEntity() } }

        override suspend fun insertQuery(query: String) {
            queriesDao.insertQuery(DatabaseSearchQuery(query = query, timestamp = Date()))
        }

        override suspend fun removeQuery(searchQuery: SearchQuery) {
            CoroutineScope(ioDispatcher).launch {
                queriesDao.removeQuery(searchQuery.toDatabaseSearchQuery())
            }
        }
    }
