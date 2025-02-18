package com.puskal.friends

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.puskal.core.DestinationRoute


fun NavGraphBuilder.friendsNavGraph(navController: NavController) {
    composable(route = DestinationRoute.FRIENDS_ROUTE) {
        FriendsScreen(navController)
    }
}