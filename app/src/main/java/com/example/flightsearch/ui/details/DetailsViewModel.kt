package com.example.flightsearch.ui.details

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flightsearch.FlightSearchApplication
import com.example.flightsearch.data.airport.AirportRepository
import com.example.flightsearch.data.favourite.Favourite
import com.example.flightsearch.data.favourite.FavouriteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailsViewModel(
    savedStateHandle: SavedStateHandle,
    airportRepository: AirportRepository,
    private val favouriteRepository: FavouriteRepository
) : ViewModel(){

    private val airportId: Int = checkNotNull(savedStateHandle[DetailsDestination.airportIdArg])

    private val airports = airportRepository
        .getAllAirports()

    private val favourites = favouriteRepository
        .getFavourites()

    val detailsUiState = airports
        .combine(favourites){
            airports, favourites ->
            DetailsUiState(
                airportCode = airports.first{it.id == airportId}.iata_code,
                favouriteList = airports
                    .map {
                        airport ->
                            Favourite(
                                departure_code = airports.first{it.id == airportId}.iata_code,
                                destination_code = airport.iata_code
                            )
                    }
                    .filter {
                        it.departure_code != it.destination_code
                    },
                databaseFavouriteList = favourites
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = DetailsUiState()
        )

    fun updateFavourites(favourite: Favourite, isFavourite: Boolean){
        if(isFavourite){
            viewModelScope.launch {
                favouriteRepository.deleteFavourite(favourite)
            }
        }
        else{
            viewModelScope.launch {
                favouriteRepository.insertFavourite(favourite)
            }
        }
    }

    companion object{
        private const val TIMEOUT_MILLIS = 5_000L
        val factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FlightSearchApplication
                DetailsViewModel(
                    this.createSavedStateHandle(),
                    application.appContainer.airportRepository,
                    application.appContainer.favouriteRepository
                )
            }
        }
    }
}

data class DetailsUiState(
    val airportCode : String = "",
    val favouriteList: List<Favourite> = listOf(),
    val databaseFavouriteList: List<Favourite> = listOf()
)