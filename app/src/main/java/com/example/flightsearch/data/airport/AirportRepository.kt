package com.example.flightsearch.data.airport

import kotlinx.coroutines.flow.Flow

interface AirportRepository {
    fun getAirports(query: String): Flow<List<Airport>>

    fun getAllAirports():  Flow<List<Airport>>
}

class DefaultAirportRepository(
    private val airportDao: AirportDao
) : AirportRepository {

    override fun getAirports(query: String): Flow<List<Airport>> {
        return airportDao.getAirports(query)
    }

    override fun getAllAirports(): Flow<List<Airport>> {
        return airportDao.getAllAirports()
    }
}