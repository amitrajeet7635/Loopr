package com.loopr.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import android.net.Uri
import androidx.navigation.compose.composable
import com.loopr.ui.presentation.viewmodel.AuthState
import com.loopr.ui.presentation.viewmodel.AuthViewModel

@Composable
fun LooprNavigation(
    navController: NavHostController = rememberNavController(),
    deepLinkUri: Uri? = null,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsState()

    // Determine start destination based on authentication state
    val startDestination = when (authState) {
        AuthState.Loading -> LooprDestinations.LOADING // Show proper loading screen
        AuthState.Authenticated -> LooprDestinations.MAIN_GRAPH
        AuthState.Unauthenticated -> LooprDestinations.AUTH_GRAPH
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Loading screen for authentication checks
        composable(LooprDestinations.LOADING) {
            com.loopr.ui.presentation.components.LooprSplashScreen()
        }

        // Authentication navigation graph
        authNavigation(
            navController = navController,
            deepLinkUri = deepLinkUri,
            authViewModel = authViewModel
        )

        // Main app navigation graph (Home, Subscriptions, Profile, etc.)
        mainNavigation(navController, authViewModel)

    }
}
