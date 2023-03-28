package com.example.flightsearch.ui.search

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flightsearch.FlightSearchApplication
import com.example.flightsearch.data.airport.AirportRepository
import com.example.flightsearch.data.favourite.Favourite
import com.example.flightsearch.data.favourite.FavouriteRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class SearchViewModel(
    private val airportRepository: AirportRepository,
    private val favouriteRepository: FavouriteRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(){

    val searchText = savedStateHandle.getStateFlow("searchText","")

    val airports = searchText
        .filterNot { it.isEmpty() }
        .debounce(500)
        .flatMapLatest {
            airportRepository.getAirports(it)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = listOf()
        )

    val favourites = favouriteRepository
        .getFavourites()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = listOf()
        )


    fun onSearchTextChange(query: String) {
        savedStateHandle["searchText"] = query
    }

    fun deleteFavourite(favourite: Favourite, isFavourite: Boolean){
        if(isFavourite){
            viewModelScope.launch{
                favouriteRepository.deleteFavourite(favourite)
            }
        }
    }

    companion object{
        private const val TIMEOUT_MILLIS = 5_000L
        val factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FlightSearchApplication
                SearchViewModel(
                    application.appContainer.airportRepository,
                    application.appContainer.favouriteRepository,
                    this.createSavedStateHandle()
                )
            }
        }
    }


}



