package com.loopr.navigation

import android.net.Uri
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.loopr.ui.presentation.screens.SignInScreen
import com.loopr.ui.presentation.screens.EmailVerificationScreen
import com.loopr.ui.presentation.viewmodel.AuthViewModel
import com.loopr.data.auth.Web3AuthManager
import javax.inject.Inject

fun NavGraphBuilder.authNavigation(
    navController: NavHostController,
    deepLinkUri: Uri? = null,
    authViewModel: AuthViewModel
) {
    navigation(
        startDestination = LooprDestinations.SIGN_IN,
        route = LooprDestinations.AUTH_GRAPH
    ) {
        composable(
            LooprDestinations.SIGN_IN,
            deepLinks = listOf(navDeepLink { uriPattern = "com.loopr://auth" })
        ) {
            // Get Web3AuthManager from the existing authViewModel since it already has access to it
            SignInScreen(
                resultUri = deepLinkUri,
                authViewModel = authViewModel,
                web3AuthManager = authViewModel.web3AuthManager,
                onEmailSubmitted = { email ->
                    navController.navigate("${LooprDestinations.EMAIL_VERIFICATION}/$email")
                },
                onAuthenticationSuccess = {
                    // Navigate to main app when Web3Auth succeeds
                    navController.navigate(LooprDestinations.MAIN_GRAPH) {
                        popUpTo(LooprDestinations.AUTH_GRAPH) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable("${LooprDestinations.EMAIL_VERIFICATION}/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            EmailVerificationScreen(
                email = email,
                onBackPressed = {
                    navController.popBackStack()
                },
                onCodeVerified = { verificationCode ->
                    // Navigate to main app after email verification
                    navController.navigate(LooprDestinations.MAIN_GRAPH) {
                        popUpTo(LooprDestinations.AUTH_GRAPH) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}
