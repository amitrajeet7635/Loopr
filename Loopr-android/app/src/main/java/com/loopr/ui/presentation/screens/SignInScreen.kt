package com.loopr.ui.presentation.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.loopr.ui.presentation.viewmodel.AuthViewModel
import com.loopr.ui.theme.LooprCyan
import com.loopr.ui.theme.LooprCyanVariant
import com.loopr.data.auth.Web3AuthManager
import com.loopr.data.auth.Web3AuthState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    resultUri: Uri? = null,
    authViewModel: AuthViewModel,
    web3AuthManager: Web3AuthManager,
    onEmailSubmitted: (String) -> Unit = {},
    onAuthenticationSuccess: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var loadingMessage by remember { mutableStateOf("") }

    val isValidEmail = email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val snackbarHostState = remember { SnackbarHostState() }

    // Observe Web3Auth state
    val web3AuthState by web3AuthManager.authState.collectAsState()

    // Handle deep link result through Web3AuthManager
    LaunchedEffect(resultUri) {
        if (resultUri != null) {
            Log.d("SignInScreen", "Processing deep link: $resultUri")
            web3AuthManager.setResultUrl(resultUri)
        }
    }

    // Handle Web3Auth state changes
    LaunchedEffect(web3AuthState) {
        when (val currentState = web3AuthState) {
            is Web3AuthState.Loading -> {
                isLoading = true
                loadingMessage = "Authenticating..."
            }
            is Web3AuthState.Authenticated -> {
                val userInfo = web3AuthManager.userInfo.value
                if (userInfo != null) {
                    Log.d("SignInScreen", "Authentication successful: $userInfo")

                    // Create UserProfile from Web3Auth userInfo
                    val userProfile = com.loopr.data.model.UserProfile(
                        emailId = userInfo.email ?: "",
                        name = userInfo.name ?: "",
                        profileImageUrl = userInfo.profileImage ?: "",
                        web3AuthId = userInfo.verifierId ?: ""
                    )

                    // Save session and profile through authViewModel
                    authViewModel.onWeb3AuthSuccess(userInfo.verifierId ?: "", userProfile)

                    // Stop loading and navigate to home
                    isLoading = false
                    loadingMessage = ""
                    onAuthenticationSuccess()
                } else {
                    isLoading = false
                    loadingMessage = ""
                    snackbarHostState.showSnackbar(
                        message = "Authentication failed: No user information received",
                        actionLabel = "OK",
                        duration = SnackbarDuration.Long
                    )
                }
            }
            is Web3AuthState.Error -> {
                isLoading = false
                loadingMessage = ""
                snackbarHostState.showSnackbar(
                    message = "Sign in failed: ${currentState.message}",
                    actionLabel = "OK",
                    duration = SnackbarDuration.Long
                )
            }
            is Web3AuthState.Idle -> {
                // Do nothing, waiting for user action
            }
        }
    }

    // Handle email submission
    fun handleEmailSubmission() {
        isLoading = true
        loadingMessage = "Preparing verification..."

        // Simulate email sending process
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000) // Brief loading to show processing
            isLoading = false
            loadingMessage = ""
            // Navigate to email verification screen
            onEmailSubmitted(email)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(modifier = Modifier.height(64.dp))

            // Loopr Logo and Brand
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 48.dp)
            ) {
                // Brand Logo
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(LooprCyan, LooprCyanVariant)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "L",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Loopr",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = "Subscriptions made simple",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Sign In Title
            Text(
                text = "Sign in",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Sign In Options
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Email Input Field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Continue with email...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Email,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (isValidEmail && !isLoading) {
                                handleEmailSubmission()
                            }
                        }
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LooprCyan,
                        cursorColor = LooprCyan
                    ),
                    singleLine = true,
                    enabled = !isLoading
                )

                // Submit Button
                Button(
                    onClick = {
                        if (isValidEmail && !isLoading) {
                            handleEmailSubmission()
                        }
                    },
                    enabled = isValidEmail && !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isValidEmail) LooprCyan else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                        contentColor = if (isValidEmail) Color.Black else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        disabledContainerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                        disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = if (isValidEmail) 8.dp else 0.dp,
                        pressedElevation = if (isValidEmail) 12.dp else 0.dp,
                        disabledElevation = 0.dp
                    ),
                    border = if (!isValidEmail) androidx.compose.foundation.BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    ) else null
                ) {
                    Text(
                        text = "Get Started",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isValidEmail) Color.Black else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                }

                // Divider with "OR"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                    Text(
                        text = "OR",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                }

                // Continue with Google
                SignInButton(
                    text = "Continue with Google",
                    onClick = {
                        if (!isLoading) {
                            web3AuthManager.loginWithGoogle()
                        }
                    },
                    iconRes = com.loopr.R.drawable.google_logo,
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    textColor = MaterialTheme.colorScheme.onSurface,
                    borderColor = MaterialTheme.colorScheme.outline,
                    enabled = !isLoading
                )

                // Continue with MetaMask (placeholder)
                SignInButton(
                    text = "Continue with MetaMask",
                    onClick = {
                        if (!isLoading) {
                            CoroutineScope(Dispatchers.Main).launch {
                                snackbarHostState.showSnackbar(
                                    message = "MetaMask integration coming soon",
                                    actionLabel = "OK",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    },
                    iconRes = com.loopr.R.drawable.metamask_icon_fox,
                    backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                    textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    borderColor = MaterialTheme.colorScheme.outline,
                    enabled = !isLoading
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        // Center Loading Overlay
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.padding(32.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = LooprCyan,
                            modifier = Modifier.size(48.dp),
                            strokeWidth = 4.dp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = loadingMessage,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // Snackbar for notifications
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun SignInButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    iconRes: Int? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    borderColor: Color? = null,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(enabled = enabled && !isLoading) { if (enabled && !isLoading) onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (enabled) backgroundColor else backgroundColor.copy(alpha = 0.6f)
        ),
        border = borderColor?.let { androidx.compose.foundation.BorderStroke(1.dp, it) },
        elevation = CardDefaults.cardElevation(defaultElevation = if (enabled) 2.dp else 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Icon
            when {
                icon != null -> {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (enabled) textColor else textColor.copy(alpha = 0.6f),
                        modifier = Modifier.size(24.dp)
                    )
                }
                iconRes != null -> {
                    Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        alpha = if (enabled) 1f else 0.6f
                    )
                }
            }

            if (icon != null || iconRes != null) {
                Spacer(modifier = Modifier.width(12.dp))
            }

            if (isLoading) {
                CircularProgressIndicator(
                    color = textColor,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = text,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (enabled) textColor else textColor.copy(alpha = 0.6f)
                )
            }
        }
    }
}
