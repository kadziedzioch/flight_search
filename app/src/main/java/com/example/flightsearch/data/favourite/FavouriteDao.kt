package com.example.flightsearch.data.favourite

import androidx.room.*
import com.example.flightsearch.data.favourite.Favourite
import kotlinx.coroutines.flow.Flow


@Dao
interface FavouriteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavourite(favourite: Favourite)

    @Delete
    suspend fun deleteFavourite(favourite: Favourite)

    @Query("SELECT * from favorite")
    fun getAllFavourites() : Flow<List<Favourite>>
}