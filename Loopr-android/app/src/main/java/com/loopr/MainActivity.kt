package com.loopr

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.loopr.data.auth.Web3AuthManager
import com.loopr.navigation.LooprNavigation
import com.loopr.ui.presentation.viewmodel.AuthViewModel
import com.loopr.ui.theme.LooprTheme
import com.web3auth.core.Web3Auth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var web3AuthManager: Web3AuthManager

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Log.d("MainActivity", "onCreate called")

        // Get deep link URI from intent
        val deepLinkUri = intent?.data
        Log.d("MainActivity", "Deep link URI: $deepLinkUri")

        // Initialize Web3Auth
        web3AuthManager.initialize()

        setContent {
            LooprTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    LooprNavigation(deepLinkUri = deepLinkUri)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("MainActivity", "onNewIntent called with data: ${intent.data}")
        // Handle user signing in when app is active
        handleDeepLink(intent.data)
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume called")
        // Handle case where user closed the browser
        if (Web3Auth.getCustomTabsClosed()) {
            Log.d("MainActivity", "Custom tabs were closed - setting flag")
            // Don't reset here, let the screen handle it
            // Just log that it happened
        }
    }

    private fun handleDeepLink(uri: Uri?) {
        Log.d("MainActivity", "handleDeepLink called with URI: $uri")
        if (uri != null) {
            Log.d("MainActivity", "Setting result URL: $uri")
            Log.d("MainActivity", "URI scheme: ${uri.scheme}, host: ${uri.host}, path: ${uri.path}")
            web3AuthManager.setResultUrl(uri)
        } else {
            Log.d("MainActivity", "No deep link URI found")
        }
    }
}
