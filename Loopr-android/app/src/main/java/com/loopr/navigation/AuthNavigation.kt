package com.loopr.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.loopr.ui.presentation.screens.SignInScreen
import com.loopr.ui.presentation.screens.EmailVerificationScreen
import com.loopr.ui.presentation.viewmodel.AuthViewModel

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
            SignInScreen(
                resultUri = deepLinkUri,
                authViewModel = authViewModel,
                onEmailSubmitted = { email ->
                    navController.navigate("${LooprDestinations.EMAIL_VERIFICATION}/$email")
                },
                onGoogleSignIn = {
                    // This will be handled by Web3AuthManager
                },
                onMetaMaskSignIn = {
                    // Handle MetaMask sign in
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
