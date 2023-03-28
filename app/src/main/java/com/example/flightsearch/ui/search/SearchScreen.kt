package com.example.flightsearch.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearch.data.airport.Airport
import com.example.flightsearch.data.favourite.Favourite
import com.example.flightsearch.navigation.NavigationDestination
import com.example.flightsearch.ui.FlightSearchTopAppBar
import com.example.flightsearch.ui.details.FavouriteListItem

object SearchDestination: NavigationDestination{
    override val route: String = "search"
    override val title: String = "Search Airport"
}

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = viewModel(factory = SearchViewModel.factory),
    navigateToDetails: (Airport)-> Unit
){
    val airports by viewModel.airports.collectAsState()
    val searchText by viewModel.searchText.collectAsState()
    val favourites by viewModel.favourites.collectAsState()

    Scaffold(
        topBar = {
            FlightSearchTopAppBar(
                title = SearchDestination.title,
                canNavigateBack = false
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier.padding(paddingValues)
        ) {
            SearchBar(
                query = searchText,
                onValueChange = viewModel::onSearchTextChange,
                isEnabled = true
            )
            if(searchText.isNotBlank()){
                AirportsList(
                    airports = airports,
                    onAirportClicked = navigateToDetails
                )
            }
            else if(favourites.isNotEmpty()){
                HomeFavouritesList(
                    favourites = favourites,
                    onFavouriteButtonClicked = viewModel::deleteFavourite
                )
            }
            else{
                Text(
                    text = "Search airports",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun HomeFavouritesList(
    favourites: List<Favourite>,
    onFavouriteButtonClicked: (Favourite, Boolean)->Unit,
    modifier: Modifier = Modifier
){
    Column(modifier = modifier){
        Text(
            text = "Favourite routes",
            modifier = Modifier.padding(10.dp),
            fontWeight = FontWeight.Bold
        )
        LazyColumn{
            items(favourites){
                    favourite ->
                FavouriteListItem(
                    favourite = favourite,
                    isFavourite = true,
                    onFavouriteButtonClicked = onFavouriteButtonClicked
                )
            }
        }
    }

}

@Composable
fun SearchBar(
    query: String,
    onValueChange: (String) -> Unit,
    isEnabled: Boolean,
    modifier: Modifier = Modifier
){
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp),
        value = query,
        enabled = isEnabled,
        onValueChange = {onValueChange(it)},
        label = { Text("Enter airport name") }
    )
}

@Composable
fun AirportsList(
    airports: List<Airport>,
    onAirportClicked: (Airport) -> Unit,
    modifier: Modifier = Modifier
){
    LazyColumn{
        items(airports, key = {it.id}){
            airport ->
            AirportListItem(
                airport = airport,
                onAirportClicked = onAirportClicked
            )
        }
    }
}

@Composable
fun AirportListItem(
    airport: Airport,
    onAirportClicked : (Airport)->Unit,
    modifier: Modifier = Modifier,
){
    Row(
        modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable { onAirportClicked(airport) }
    ) {
        Text(
            text = airport.iata_code,
            fontWeight = FontWeight.Bold
        )
        Spacer(
            modifier = Modifier.width(5.dp)
        )
        Text(
            text = airport.name
        )

    }
}

