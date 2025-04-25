package br.pedroso.citieslist.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext applicationContext: Context,
        databaseInitializationCallback: RoomDatabase.Callback,
    ): AppDatabase {
        val databaseBuilder =
            Room.databaseBuilder(
                context = applicationContext,
                klass = AppDatabase::class.java,
                name = "cities-database",
            )

        return databaseBuilder
            .addCallback(databaseInitializationCallback)
            .build()
    }

    @Provides
    fun provideCitiesDao(appDatabase: AppDatabase): CitiesDao {
        return appDatabase.citiesDao()
    }

    @Provides
    fun provideQueriesDao(appDatabase: AppDatabase): QueriesDao {
        return appDatabase.queriesDao()
    }
}
