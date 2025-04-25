package br.pedroso.citieslist.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "queries")
data class DatabaseSearchQuery(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "query") val query: String,
    @ColumnInfo(name = "timestamp") val timestamp: Date,
)
