package com.loopr.app.navigation

import android.net.Uri
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.loopr.app.ui.presentation.screens.HomeScreen
import com.loopr.app.ui.presentation.screens.SignInScreen

@Composable
fun LooprNavigation(
    innerPaddingValues: PaddingValues,
    navController: NavHostController = rememberNavController(),
    deepLinkUri: Uri? = null
) {
    // Always start with SignInScreen - it will handle session checking and routing internally
    val startDestination = LooprDestinations.SIGN_IN

    NavHost(
        navController = navController, startDestination = startDestination
    ) {
        composable(
            LooprDestinations.SIGN_IN,
            deepLinks = listOf(navDeepLink { uriPattern = "com.loopr.app://auth" })
        ) {
            SignInScreen(
                deepLinkUri = deepLinkUri, onAuthenticationSuccess = {
                    navController.navigate(LooprDestinations.HOME) {
                        popUpTo(LooprDestinations.SIGN_IN) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                })
        }


        composable(LooprDestinations.HOME) {
            HomeScreen()
        }

    }
}
