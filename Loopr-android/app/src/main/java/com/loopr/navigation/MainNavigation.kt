package com.loopr.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.loopr.ui.presentation.screens.HomeScreen
import com.loopr.ui.presentation.viewmodel.AuthViewModel

fun NavGraphBuilder.mainNavigation(
    navController: NavHostController, authViewModel: AuthViewModel
) {
    navigation(
        startDestination = LooprDestinations.HOME, route = LooprDestinations.MAIN_GRAPH
    ) {
        composable(LooprDestinations.HOME) {
            // Use the actual HomeScreen from HomeScreen.kt with authViewModel
            HomeScreen(authViewModel = authViewModel)
        }

        composable(LooprDestinations.SUBSCRIPTIONS) {
            // SubscriptionsScreen() - Create this later
            // Placeholder for now
        }

        composable(LooprDestinations.PROFILE) {
            // ProfileScreen(authViewModel = authViewModel) - Create this later
            // This screen can use authViewModel to show user profile and logout functionality
        }
    }
}