package br.pedroso.citieslist.queryhistory

import br.pedroso.citieslist.database.DatabaseSearchQuery
import br.pedroso.citieslist.domain.SearchQuery

internal fun SearchQuery.toDatabaseSearchQuery(): DatabaseSearchQuery =
    DatabaseSearchQuery(
        id = id,
        query = query,
        timestamp = timestamp,
    )

internal fun DatabaseSearchQuery.toEntity(): SearchQuery =
    SearchQuery(
        id = id,
        query = query,
        timestamp = timestamp,
    )
