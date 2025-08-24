package com.loopr.data.auth

import android.content.Context
import android.net.Uri
import android.util.Log
import com.web3auth.core.Web3Auth
import com.web3auth.core.types.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.CompletableFuture
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Web3AuthManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var web3Auth: Web3Auth? = null

    private val _authState = MutableStateFlow<Web3AuthState>(Web3AuthState.Idle)
    val authState: StateFlow<Web3AuthState> = _authState.asStateFlow()

    private val _userInfo = MutableStateFlow<UserInfo?>(null)
    val userInfo: StateFlow<UserInfo?> = _userInfo.asStateFlow()

    fun initialize() {
        try {
            web3Auth = Web3Auth(
                Web3AuthOptions(
                    clientId = "BB7pTH54rSoTxEGLATfmTaqzPugFNo3dsW84i1Z5Tg0FNJdGb_TewcAw5Y_9oB5vxQ4JTM8psVdRJYwXsBfpRxw",
                    network = Network.SAPPHIRE_DEVNET,
                    buildEnv = BuildEnv.PRODUCTION,
                    redirectUrl = Uri.parse("com.loopr://auth")
                ),
                context
            )

            val sessionResponse: CompletableFuture<Void> = web3Auth!!.initialize()
            sessionResponse.whenComplete { _, error ->
                if (error == null) {
                    checkExistingSession()
                } else {
                    Log.e("Web3AuthManager", "Initialization error: ${error.message}")
                    _authState.value = Web3AuthState.Error(error.message ?: "Initialization failed")
                }
            }
        } catch (e: Exception) {
            Log.e("Web3AuthManager", "Failed to initialize Web3Auth", e)
            _authState.value = Web3AuthState.Error("Failed to initialize Web3Auth")
        }
    }

    fun loginWithGoogle() {
        _authState.value = Web3AuthState.Loading

        val loginParams = LoginParams(
            Provider.GOOGLE,
            extraLoginOptions = ExtraLoginOptions()
        )

        val loginFuture: CompletableFuture<Web3AuthResponse> = web3Auth?.login(loginParams)
            ?: run {
                _authState.value = Web3AuthState.Error("Web3Auth not initialized")
                return
            }

        loginFuture.whenComplete { response, error ->
            if (error == null && response != null) {
                _authState.value = Web3AuthState.Authenticated(response)
                _userInfo.value = web3Auth?.getUserInfo()
                Log.d("Web3AuthManager", "Login successful")
                Log.d("Web3AuthManager", "Private Key: ${web3Auth?.getPrivkey()}")
                Log.d("Web3AuthManager", "User Info: ${web3Auth?.getUserInfo()}")
            } else {
                Log.e("Web3AuthManager", "Login error: ${error?.message}")
                _authState.value = Web3AuthState.Error(error?.message ?: "Login failed")
            }
        }
    }

    fun loginWithEmail(email: String) {
        _authState.value = Web3AuthState.Loading

        val loginParams = LoginParams(
            Provider.EMAIL_PASSWORDLESS,
            extraLoginOptions = ExtraLoginOptions(login_hint = email)
        )

        val loginFuture: CompletableFuture<Web3AuthResponse> = web3Auth?.login(loginParams)
            ?: run {
                _authState.value = Web3AuthState.Error("Web3Auth not initialized")
                return
            }

        loginFuture.whenComplete { response, error ->
            if (error == null && response != null) {
                _authState.value = Web3AuthState.Authenticated(response)
                _userInfo.value = web3Auth?.getUserInfo()
                Log.d("Web3AuthManager", "Email login successful")
            } else {
                Log.e("Web3AuthManager", "Email login error: ${error?.message}")
                _authState.value = Web3AuthState.Error(error?.message ?: "Email login failed")
            }
        }
    }

    fun logout() {
        val logoutFuture = web3Auth?.logout()
        logoutFuture?.whenComplete { _, error ->
            if (error == null) {
                _authState.value = Web3AuthState.Idle
                _userInfo.value = null
                Log.d("Web3AuthManager", "Logout successful")
            } else {
                Log.e("Web3AuthManager", "Logout error: ${error.message}")
            }
        }
    }

    fun setResultUrl(uri: Uri?) {
        web3Auth?.setResultUrl(uri)
    }

    fun getPrivateKey(): String? {
        return try {
            web3Auth?.getPrivkey()
        } catch (e: Exception) {
            Log.e("Web3AuthManager", "Error getting private key", e)
            null
        }
    }

    fun getEd25519PrivateKey(): String? {
        return try {
            web3Auth?.getEd25519PrivKey()
        } catch (e: Exception) {
            Log.e("Web3AuthManager", "Error getting ed25519 private key", e)
            null
        }
    }

    private fun checkExistingSession() {
        try {
            val privateKey = web3Auth?.getPrivkey()
            val userInfo = web3Auth?.getUserInfo()

            if (!privateKey.isNullOrEmpty()) {
                _authState.value = Web3AuthState.Authenticated(
                    Web3AuthResponse(
                        privKey = privateKey,
                        userInfo = userInfo
                    )
                )
                _userInfo.value = userInfo
                Log.d("Web3AuthManager", "Existing session found")
            } else {
                _authState.value = Web3AuthState.Idle
                Log.d("Web3AuthManager", "No existing session")
            }
        } catch (e: Exception) {
            Log.e("Web3AuthManager", "Error checking existing session", e)
            _authState.value = Web3AuthState.Idle
        }
    }
}

sealed class Web3AuthState {
    object Idle : Web3AuthState()
    object Loading : Web3AuthState()
    data class Authenticated(val response: Web3AuthResponse) : Web3AuthState()
    data class Error(val message: String) : Web3AuthState()
}
