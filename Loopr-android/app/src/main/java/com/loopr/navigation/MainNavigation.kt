package com.loopr.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.loopr.ui.presentation.viewmodel.AuthViewModel
import com.loopr.ui.presentation.screens.HomeScreen

fun NavGraphBuilder.mainNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    navigation(
        startDestination = LooprDestinations.HOME,
        route = LooprDestinations.MAIN_GRAPH
    ) {
        composable(LooprDestinations.HOME) {
            // Use the actual HomeScreen from HomeScreen.kt
            HomeScreen()
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
