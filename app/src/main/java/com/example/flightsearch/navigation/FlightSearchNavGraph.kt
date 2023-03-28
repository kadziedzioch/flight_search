package com.example.flightsearch.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.flightsearch.ui.details.DetailsDestination
import com.example.flightsearch.ui.details.DetailsScreen
import com.example.flightsearch.ui.search.SearchDestination
import com.example.flightsearch.ui.search.SearchScreen


@Composable
fun FlightSearchNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = SearchDestination.route,
        modifier = modifier
    )
    {
        composable(route = SearchDestination.route){
            SearchScreen(
                navigateToDetails = {navController.navigate("${DetailsDestination.route}/${it.id}")}
            )
        }
        composable(
            route= DetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(DetailsDestination.airportIdArg){
                type = NavType.IntType
            })
        ){
            DetailsScreen(
                onNavigateUp = { navController.navigateUp() }
            )
        }


    }

}