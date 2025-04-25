package br.pedroso.citieslist.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QueriesDao {
    @Query("SELECT * FROM queries ORDER BY timestamp DESC LIMIT 5")
    fun getAllQueries(): Flow<List<DatabaseSearchQuery>>

    @Insert
    suspend fun insertQuery(query: DatabaseSearchQuery)

    @Delete
    suspend fun removeQuery(toDatabaseSearchQuery: DatabaseSearchQuery)
}
