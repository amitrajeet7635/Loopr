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
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    val web3AuthManager: Web3AuthManager  // Make this public so AuthNavigation can access it
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    init {
        checkAuthenticationStatus()
    }

    private fun checkAuthenticationStatus() {
        viewModelScope.launch {
            var newAuthState: AuthState = AuthState.Loading
            try {
                val savedSession = userRepository.getWeb3AuthSession()

                if (savedSession != null && savedSession.isNotEmpty()) {
                    // Check if we have user profile data saved locally
                    try {
                        val profile = userRepository.getUserProfile().first()
                        if (profile.web3AuthId.isNotEmpty()) {
                            // ✅ We have saved user data - consider user logged in
                            _userProfile.value = profile
                            newAuthState = AuthState.Authenticated
                        } else {
                            // ❌ No valid user profile → logout and show login screen
                            handleSessionExpired()
                            newAuthState = AuthState.Unauthenticated
                        }
                    } catch (e: Exception) {
                        Log.e("AuthViewModel", "Error getting user profile: ${e.message}")
                        handleSessionExpired()
                        newAuthState = AuthState.Unauthenticated
                    }
                } else {
                    // ❌ No session saved → show login screen
                    newAuthState = AuthState.Unauthenticated
                }
            } catch (e: Exception) {
                // Handle any errors during session check
                Log.e("AuthViewModel", "Error during authentication check: ${e.message}")
                handleSessionExpired()
                newAuthState = AuthState.Unauthenticated
            }
            _authState.value = newAuthState
        }
    }

    private suspend fun handleSessionExpired() {
        userRepository.logout()
        _userProfile.value = UserProfile()
        // Remove this line - let the caller handle setting the auth state
        // _authState.value = AuthState.Unauthenticated
    }

    fun onWeb3AuthSuccess(sessionData: String, userProfile: UserProfile) {
        viewModelScope.launch {
            // Save the Web3Auth session securely
            userRepository.saveWeb3AuthSession(sessionData)

            // Save user profile data (non-sensitive)
            userRepository.saveUserProfile(userProfile)

            _userProfile.value = userProfile
            _authState.value = AuthState.Authenticated
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                // Logout from Web3Auth
                web3AuthManager.logout()

                // Clear local data
                userRepository.logout()

                _userProfile.value = UserProfile()
                _authState.value = AuthState.Unauthenticated
            } catch (e: Exception) {
                // Even if Web3Auth logout fails, clear local data
                userRepository.logout()
                _userProfile.value = UserProfile()
                _authState.value = AuthState.Unauthenticated
            }
        }
    }

    fun retryAuthentication() {
        _authState.value = AuthState.Loading
        checkAuthenticationStatus()
    }
}
