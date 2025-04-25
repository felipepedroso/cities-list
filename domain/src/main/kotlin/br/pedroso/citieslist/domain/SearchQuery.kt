package br.pedroso.citieslist.domain

import java.util.Date

data class SearchQuery(
    val id: Int,
    val query: String,
    val timestamp: Date,
)
