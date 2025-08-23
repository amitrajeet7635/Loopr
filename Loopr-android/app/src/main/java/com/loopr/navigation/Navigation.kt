package com.loopr.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import android.net.Uri

@Composable
fun LooprNavigation(
    navController: NavHostController = rememberNavController(),
    deepLinkUri: Uri? = null
) {
    NavHost(
        navController = navController,
        startDestination = LooprDestinations.AUTH_GRAPH
    ) {
        // Auth navigation graph (Sign in, Email verification, etc.)
        authNavigation(navController, deepLinkUri)

        // Main app navigation graph (Home, Subscriptions, Profile, etc.)
        mainNavigation(navController)
    }
}
