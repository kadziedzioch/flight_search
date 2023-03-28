package com.example.flightsearch.ui.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearch.data.favourite.Favourite
import com.example.flightsearch.navigation.NavigationDestination
import com.example.flightsearch.ui.FlightSearchTopAppBar

object DetailsDestination: NavigationDestination {
    override val route: String = "details"
    override val title: String = "Flights"
    const val airportIdArg = "airportId"
    val routeWithArgs = "$route/{$airportIdArg}"
}

@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel = viewModel(factory = DetailsViewModel.factory),
    onNavigateUp: ()->Unit
){
    val detailsUiState by viewModel.detailsUiState.collectAsState()

    Scaffold(
        topBar = {
            FlightSearchTopAppBar(
                title = DetailsDestination.title,
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        }
    ) {
        paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            AirportCodeText(airportCode = detailsUiState.airportCode)
            FavouritesList(
                detailsUiState = detailsUiState,
                onFavouriteButtonClicked = viewModel::updateFavourites
            )
        }
    }
}
@Composable
fun AirportCodeText(
    airportCode: String,
    modifier: Modifier = Modifier
){
    Text(
        text = "Flights from $airportCode",
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp, start = 5.dp),
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )
}

@Composable
fun FavouritesList(
    detailsUiState: DetailsUiState,
    onFavouriteButtonClicked: (Favourite, Boolean)->Unit,
    modifier: Modifier = Modifier
){
    LazyColumn(modifier = modifier){
        items(detailsUiState.favouriteList){
            favourite ->
            val dbFavourite = detailsUiState.databaseFavouriteList.find {
                it.departure_code == favourite.departure_code && it.destination_code == favourite.destination_code
            }
            FavouriteListItem(
                favourite = dbFavourite ?: favourite,
                isFavourite = dbFavourite != null,
                onFavouriteButtonClicked = onFavouriteButtonClicked
            )
        }
    }
}

@Composable
fun FavouriteListItem(
    favourite: Favourite,
    isFavourite: Boolean,
    onFavouriteButtonClicked: (Favourite, Boolean)->Unit,
    modifier: Modifier = Modifier
){
    Card(
        elevation = 4.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Row(modifier = Modifier.padding(10.dp)) {
            FlightColumn(
                isDestination = false,
                favourite = favourite
            )
            Text(
                text = "-",
                fontSize = 18.sp,
                modifier = Modifier.padding(10.dp)
            )
            FlightColumn(
                isDestination = true,
                favourite = favourite
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {
                    onFavouriteButtonClicked(favourite, isFavourite)
                },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = if (isFavourite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    tint = if (isFavourite) Color.Red else Color.LightGray,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun FlightColumn(
    isDestination: Boolean,
    favourite: Favourite,
    modifier: Modifier = Modifier

) {
    Column(modifier = modifier) {
        Text(
            text = if (isDestination) "ARRIVE" else "DEPART",
            fontSize = 10.sp
        )
        Spacer(modifier = modifier.height(3.dp))
        Text(
            text = if (isDestination) favourite.destination_code else favourite.departure_code,
            fontSize = 18.sp
        )
    }

}







