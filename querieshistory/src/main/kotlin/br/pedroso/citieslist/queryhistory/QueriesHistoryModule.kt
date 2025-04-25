package br.pedroso.citieslist.queryhistory

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class QueriesHistoryModule {
    @Binds
    @Singleton
    abstract fun bindQueriesRepository(queriesHistoryRepositoryImpl: QueriesHistoryRepositoryImpl): QueriesHistoryRepository
}
