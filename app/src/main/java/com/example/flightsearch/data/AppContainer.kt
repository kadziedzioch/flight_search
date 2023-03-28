package com.example.flightsearch.data

import android.content.Context
import com.example.flightsearch.data.airport.AirportRepository
import com.example.flightsearch.data.airport.DefaultAirportRepository
import com.example.flightsearch.data.favourite.DefaultFavouriteRepository
import com.example.flightsearch.data.favourite.FavouriteRepository

interface AppContainer {
    val airportRepository : AirportRepository
    val favouriteRepository : FavouriteRepository
}

class DefaultAppContainer(private val context: Context): AppContainer {


    override val airportRepository: AirportRepository by lazy {
        DefaultAirportRepository(FlightDatabase.getDatabase(context).airportDao())
    }
    override val favouriteRepository: FavouriteRepository by lazy{
        DefaultFavouriteRepository(FlightDatabase.getDatabase(context).favouriteDao())
    }

}