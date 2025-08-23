package com.loopr.ui.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loopr.data.auth.Web3AuthManager
import com.loopr.data.auth.Web3AuthState
import com.web3auth.core.types.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val web3AuthManager: Web3AuthManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    val authState = web3AuthManager.authState
    val userInfo = web3AuthManager.userInfo

    init {
        // Observe auth state changes
        viewModelScope.launch {
            web3AuthManager.authState.collect { state ->
                _uiState.value = _uiState.value.copy(
                    isLoading = state is Web3AuthState.Loading,
                    error = if (state is Web3AuthState.Error) state.message else null
                )
            }
        }
    }

    fun onEmailChanged(email: String) {
        _uiState.value = _uiState.value.copy(
            email = email,
            error = null
        )
    }

    fun onEmailSubmitted() {
        val email = _uiState.value.email.trim()
        if (email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            web3AuthManager.loginWithEmail(email)
        } else {
            _uiState.value = _uiState.value.copy(
                error = "Please enter a valid email address"
            )
        }
    }

    fun onGoogleSignIn() {
        web3AuthManager.loginWithGoogle()
    }

    fun onMetaMaskSignIn() {
        // TODO: Implement MetaMask integration
        _uiState.value = _uiState.value.copy(
            error = "MetaMask integration coming soon"
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun logout() {
        web3AuthManager.logout()
    }
}

data class AuthUiState(
    val email: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
