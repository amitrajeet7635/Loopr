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

fun NavGraphBuilder.mainNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    navigation(
        startDestination = LooprDestinations.HOME,
        route = LooprDestinations.MAIN_GRAPH
    ) {
        composable(LooprDestinations.HOME) {
            // Temporary HomeScreen until you create the actual one
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

@Composable
private fun HomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🎉 Authentication Successful!",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Welcome to Loopr",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
