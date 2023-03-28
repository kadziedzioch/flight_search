package com.example.flightsearch.data.favourite

import com.example.flightsearch.data.airport.Airport
import com.example.flightsearch.data.airport.AirportDao
import kotlinx.coroutines.flow.Flow

interface FavouriteRepository {
    fun getFavourites(): Flow<List<Favourite>>

    suspend fun insertFavourite(favourite: Favourite)

    suspend fun deleteFavourite(favourite: Favourite)
}

class DefaultFavouriteRepository(
    private val favouriteDao: FavouriteDao
) : FavouriteRepository {

    override fun getFavourites(): Flow<List<Favourite>> {
        return favouriteDao.getAllFavourites()
    }

    override suspend fun insertFavourite(favourite: Favourite) {
        favouriteDao.insertFavourite(favourite)
    }

    override suspend fun deleteFavourite(favourite: Favourite) {
        favouriteDao.deleteFavourite(favourite)
    }


}