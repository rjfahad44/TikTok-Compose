package com.puskal.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.puskal.core.DestinationRoute.HOME_SCREEN_ROUTE



fun NavGraphBuilder.homeNavGraph(navController: NavController) {
    composable(route = HOME_SCREEN_ROUTE) {
        HomeScreen(navController)
    }
}