package com.loopr.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.mainNavigation(
    navController: NavHostController
) {
    navigation(
        startDestination = LooprDestinations.HOME,
        route = LooprDestinations.MAIN_GRAPH
    ) {
        composable(LooprDestinations.HOME) {
            // HomeScreen() - Create this later
            // Placeholder for now
        }

        composable(LooprDestinations.SUBSCRIPTIONS) {
            // SubscriptionsScreen() - Create this later
            // Placeholder for now
        }

        composable(LooprDestinations.PROFILE) {
            // ProfileScreen() - Create this later
            // Placeholder for now
        }
    }
}
