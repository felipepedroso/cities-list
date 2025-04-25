package br.pedroso.citieslist.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [DatabaseCity::class, DatabaseSearchQuery::class], version = 1, exportSchema = false)
@TypeConverters(DateTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun citiesDao(): CitiesDao

    abstract fun queriesDao(): QueriesDao
}
