package com.example.flightsearch.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flightsearch.data.airport.Airport
import com.example.flightsearch.data.airport.AirportDao
import com.example.flightsearch.data.favourite.Favourite
import com.example.flightsearch.data.favourite.FavouriteDao

@Database(entities = [Airport::class,Favourite::class], version = 1, exportSchema = false)
abstract class FlightDatabase : RoomDatabase() {

    abstract fun airportDao() : AirportDao
    abstract fun favouriteDao(): FavouriteDao

    companion object {
        @Volatile
        private var Instance: FlightDatabase? = null
        fun getDatabase(context: Context): FlightDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, FlightDatabase::class.java, "flight_search")
                    .createFromAsset("database/flight_search.db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}