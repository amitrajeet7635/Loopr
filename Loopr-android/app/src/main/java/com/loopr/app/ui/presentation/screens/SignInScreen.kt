package com.loopr.app.ui.presentation.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContextWrapper
import android.net.Uri
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import com.loopr.app.R
import com.loopr.app.ui.presentation.components.LooprLoadingUI
import com.loopr.app.ui.presentation.viewmodel.AuthViewModel
import com.loopr.app.ui.theme.LooprCyan
import com.web3auth.core.Web3Auth
import com.web3auth.core.types.BuildEnv
import com.web3auth.core.types.ExtraLoginOptions
import com.web3auth.core.types.LoginParams
import com.web3auth.core.types.Network
import com.web3auth.core.types.Provider
import com.web3auth.core.types.Web3AuthOptions

@Composable
fun getActivity(): Activity {
    val context = LocalContext.current
    return remember(context) {
        var ctx = context
        while (ctx is ContextWrapper) {
            if (ctx is Activity) return@remember ctx
            ctx = ctx.baseContext
        }
        throw IllegalStateException("Activity not found in context chain.")
    }
}

@SuppressLint("ContextCastToActivity")
@Composable
fun SignInScreen(
    deepLinkUri: Uri? = null,
    onAuthenticationSuccess: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel(),
    shouldLogout: Boolean = false
) {
    var email by remember { mutableStateOf("") }
    var isAuthenticated by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var isInitialized by remember { mutableStateOf(false) }


    val activity = getActivity()

    // Web3Auth instance
    val web3Auth = remember(activity) {
        Web3Auth(
            Web3AuthOptions(
                clientId = "BLnAilyLRnsasvdfRjlyGYT9eTuWQhWhc5rU6SVD542xQl8j5NfAn1m61b86s2sqdWosZNON65bpuH6ILmKqrhg",
                network = Network.SAPPHIRE_DEVNET,
                buildEnv = BuildEnv.PRODUCTION,
                redirectUrl = "com.loopr.app://auth".toUri()
            ), activity
        )
    }

    // Handle logout if requested
    LaunchedEffect(shouldLogout) {
        if (shouldLogout) {
            web3Auth.logout()
            authViewModel.setUserInfo(null)
        }
    }

    // Handle deep link and check session
    LaunchedEffect(deepLinkUri) {
        isInitialized = false
        web3Auth.setResultUrl(deepLinkUri)

        web3Auth.initialize().whenComplete { _, error ->
            isAuthenticated = error == null && web3Auth.getUserInfo() != null
            isInitialized = true
        }
    }

    when {
        !isInitialized -> {
            LooprLoadingUI(subtitle = "Setting things up for you...")
        }

        isAuthenticated -> {
            // Set userInfo in AuthViewModel
            authViewModel.setUserInfo(web3Auth.getUserInfo())
            onAuthenticationSuccess()
        }

        else -> {
            SignInContent(
                email = email,
                onEmailChange = { email = it },
                isLoading = isLoading,
                onLogin = { loginParams ->
                    isLoading = true
                    web3Auth.login(loginParams).whenComplete { result, error ->
                        isLoading = false
                        if (error == null && result.userInfo != null) {
                            authViewModel.setUserInfo(result.userInfo)
                            onAuthenticationSuccess()
                        } else {
                            Toast.makeText(activity, error?.message, Toast.LENGTH_LONG).show()
                        }
                    }
                })
        }
    }
}

@Composable
fun SignInContent(
    email: String,
    onEmailChange: (String) -> Unit,
    isLoading: Boolean,
    onLogin: (LoginParams) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.surface
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
                Image(
                    painterResource(id = R.drawable.loopr_logo),
                    contentDescription = "Loopr Logo",
                    modifier = Modifier.scale(0.8f)
                )

                Spacer(modifier = Modifier.height(8.dp))

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

            // Email Input Field
            OutlinedTextField(
                value = email,
                onValueChange = { onEmailChange(it) },
                placeholder = { Text("Continue with email") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email, imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            onLogin(
                                LoginParams(
                                    Provider.EMAIL_PASSWORDLESS,
                                    extraLoginOptions = ExtraLoginOptions(login_hint = email)
                                )
                            )
                        }
                    }),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = LooprCyan, cursorColor = LooprCyan
                ),
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Submit Button
            Button(
                onClick = {
                    if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        onLogin(
                            LoginParams(
                                Provider.EMAIL_PASSWORDLESS,
                                extraLoginOptions = ExtraLoginOptions(login_hint = email)
                            )
                        )
                    }
                },
                enabled = Patterns.EMAIL_ADDRESS.matcher(email).matches() && !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LooprCyan, contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Get Started", fontSize = 18.sp, fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // OR Divider
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

            // Sign in with Google and MetaMask buttons
            SignInButton(
                text = "Continue with Google",
                onClick = { onLogin(LoginParams(Provider.GOOGLE)) },
                iconRes = R.drawable.google_logo,
                backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                borderColor = MaterialTheme.colorScheme.outline,
                enabled = !isLoading
            )

            SignInButton(
                text = "Continue with MetaMask",
                onClick = { /* handle MetaMask login */ },
                iconRes = R.drawable.metamask_icon_fox,
                backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                borderColor = MaterialTheme.colorScheme.outline,
                enabled = !isLoading
            )
        }

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
                            color = LooprCyan, modifier = Modifier.size(48.dp), strokeWidth = 4.dp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Authenticating",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
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
            .padding(vertical = 6.dp)
            .fillMaxWidth()
            .height(56.dp)
            .clickable(enabled = enabled && !isLoading) { if (enabled && !isLoading) onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (enabled) backgroundColor else backgroundColor.copy(alpha = 0.6f)
        ),
        border = borderColor?.let { BorderStroke(1.dp, it) },
        elevation = CardDefaults.cardElevation(defaultElevation = if (enabled) 2.dp else 0.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Row(
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
                        color = textColor, modifier = Modifier.size(24.dp), strokeWidth = 2.dp
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
}
