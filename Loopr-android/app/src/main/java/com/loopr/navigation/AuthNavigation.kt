package com.loopr.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.loopr.ui.presentation.screens.SignInScreen
import com.loopr.ui.presentation.screens.EmailVerificationScreen

fun NavGraphBuilder.authNavigation(
    navController: NavHostController
) {
    navigation(
        startDestination = LooprDestinations.SIGN_IN,
        route = LooprDestinations.AUTH_GRAPH
    ) {
        composable(LooprDestinations.SIGN_IN) {
            SignInScreen(
                onEmailSubmitted = { email ->
                    navController.navigate("${LooprDestinations.EMAIL_VERIFICATION}/$email")
                },
                onGoogleSignIn = {
                    // Handle Google sign in
                },
                onMetaMaskSignIn = {
                    // Handle MetaMask sign in
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
                onCodeVerified = { code ->
                    // Handle code verification success
                    // Navigate to main app or handle authentication
                },
                onResendCode = {
                    // Handle resend code
                }
            )
        }
    }
}
