package com.loopr.ui.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loopr.data.auth.Web3AuthManager
import com.loopr.data.model.UserProfile
import com.loopr.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModelNew @Inject constructor(
    private val userRepository: UserRepository,
    private val web3AuthManager: Web3AuthManager
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    init {
        performAuthenticationCheck()
    }

    private fun performAuthenticationCheck() {
        viewModelScope.launch {
            val authResult = checkForExistingAuthentication()
            updateAuthenticationState(authResult)
        }
    }

    private suspend fun checkForExistingAuthentication(): AuthenticationResult {
        return try {
            val sessionExists = userRepository.hasValidWeb3AuthSession()

            if (!sessionExists) {
                Log.d("AuthViewModel", "No session found")
                return AuthenticationResult.NoSession
            }

            val profile = userRepository.getUserProfile().first()

            if (profile.web3AuthId.isEmpty()) {
                Log.d("AuthViewModel", "Invalid profile data")
                clearAuthenticationData()
                return AuthenticationResult.InvalidProfile
            }

            Log.d("AuthViewModel", "Valid authentication found")
            return AuthenticationResult.Valid(profile)

        } catch (exception: Exception) {
            Log.e("AuthViewModel", "Authentication check failed", exception)
            clearAuthenticationData()
            return AuthenticationResult.Error(exception)
        }
    }

    private fun updateAuthenticationState(result: AuthenticationResult) {
        when (result) {
            is AuthenticationResult.Valid -> {
                _userProfile.value = result.profile
                _authState.value = AuthState.Authenticated
            }
            is AuthenticationResult.NoSession,
            is AuthenticationResult.InvalidProfile,
            is AuthenticationResult.Error -> {
                _userProfile.value = UserProfile()
                _authState.value = AuthState.Unauthenticated
            }
        }
    }

    private suspend fun clearAuthenticationData() {
        try {
            userRepository.logout()
            _userProfile.value = UserProfile()
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Error clearing auth data", e)
        }
    }

    fun handleWeb3AuthSuccess(sessionData: String, userProfile: UserProfile) {
        viewModelScope.launch {
            try {
                userRepository.saveWeb3AuthSession(sessionData)
                userRepository.saveUserProfile(userProfile)

                _userProfile.value = userProfile
                _authState.value = AuthState.Authenticated

                Log.d("AuthViewModel", "Authentication success handled")
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Failed to save auth data", e)
                _authState.value = AuthState.Unauthenticated
            }
        }
    }

    fun performLogout() {
        viewModelScope.launch {
            try {
                web3AuthManager.logout()
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Web3Auth logout failed", e)
            }

            clearAuthenticationData()
            _authState.value = AuthState.Unauthenticated
        }
    }

    fun retryAuthentication() {
        _authState.value = AuthState.Loading
        performAuthenticationCheck()
    }

    private sealed class AuthenticationResult {
        object NoSession : AuthenticationResult()
        object InvalidProfile : AuthenticationResult()
        data class Valid(val profile: UserProfile) : AuthenticationResult()
        data class Error(val exception: Exception) : AuthenticationResult()
    }
}
