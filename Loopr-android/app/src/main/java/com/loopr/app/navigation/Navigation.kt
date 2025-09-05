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
import androidx.hilt.navigation.compose.hiltViewModel
import com.loopr.app.ui.presentation.viewmodel.AuthViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavType
import androidx.navigation.navArgument

@Composable
fun LooprNavigation(
    innerPaddingValues: PaddingValues,
    navController: NavHostController = rememberNavController(),
    deepLinkUri: Uri? = null
) {
    // Always start with SignInScreen - it will handle session checking and routing internally
    val startDestination = LooprDestinations.SIGN_IN

    val authViewModel: AuthViewModel = hiltViewModel()
    val userInfo by authViewModel.userInfo.collectAsState()

    NavHost(
        navController = navController, startDestination = startDestination
    ) {
        composable(
            route = LooprDestinations.SIGN_IN + "?shouldLogout={shouldLogout}",
            arguments = listOf(
                navArgument("shouldLogout") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            ),
            deepLinks = listOf(navDeepLink { uriPattern = "com.loopr.app://auth" })
        ) { backStackEntry ->
            val shouldLogout = backStackEntry.arguments?.getBoolean("shouldLogout") ?: false
            SignInScreen(
                deepLinkUri = deepLinkUri,
                onAuthenticationSuccess = {
                    navController.navigate(LooprDestinations.HOME) {
                        popUpTo(LooprDestinations.SIGN_IN) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                authViewModel = authViewModel,
                shouldLogout = shouldLogout
            )
        }


        composable(LooprDestinations.HOME) {
            HomeScreen(
                userInfo = userInfo,
                onLogout = {
                    authViewModel.logout {
                        navController.navigate(LooprDestinations.SIGN_IN + "?shouldLogout=true") {
                            popUpTo(0)
                        }
                    }
                }
            )
        }

    }
}
