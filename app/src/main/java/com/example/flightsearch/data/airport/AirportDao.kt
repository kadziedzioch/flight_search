package com.example.flightsearch.data.airport

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AirportDao {
    @Query("SELECT * from airport WHERE iata_code LIKE '%' || :query || '%' OR name LIKE '%' || :query || '%' ORDER BY passengers DESC")
    fun getAirports(query: String) : Flow<List<Airport>>

    @Query("SELECT * from airport")
    fun getAllAirports() : Flow<List<Airport>>
}