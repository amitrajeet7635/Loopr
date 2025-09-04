package com.loopr.app.ui.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.loopr.app.ui.theme.LooprCyan
import com.loopr.app.ui.theme.LooprCyanVariant
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailVerificationScreen(
    email: String = "",
    onBackPressed: () -> Unit = {},
    onCodeVerified: (String) -> Unit = {},
    onResendCode: () -> Unit = {}
) {
    var code by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var canResend by remember { mutableStateOf(false) }
    var countdown by remember { mutableStateOf(60) }

    val isValidCode = code.length == 6

    // Countdown timer for resend
    LaunchedEffect(Unit) {
        while (countdown > 0) {
            delay(1000)
            countdown--
        }
        canResend = true
    }

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
        // Top App Bar with Back Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Loopr Logo and Brand (same as SignIn)
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
                text = "Subscriptions made simple",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Verification Title
        Text(
            text = "Verify Email",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Description
        Text(
            text = "We've sent a verification code in your inbox",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Code Input
        CodeInputField(
            code = code,
            onCodeChange = { newCode ->
                if (newCode.length <= 6) {
                    code = newCode
                    if (newCode.length == 6) {
                        isLoading = true
                        onCodeVerified(newCode)
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Verify Button (matching SignIn button style)
        Button(
            onClick = {
                if (isValidCode) {
                    isLoading = true
                    onCodeVerified(code)
                }
            },
            enabled = isValidCode && !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isValidCode) LooprCyan else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                contentColor = if (isValidCode) Color.Black else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                disabledContainerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = if (isValidCode) 8.dp else 0.dp,
                pressedElevation = if (isValidCode) 12.dp else 0.dp,
                disabledElevation = 0.dp
            ),
            border = if (!isValidCode) BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            ) else null
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.Black,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.5.dp
                    )
                } else {
                    if (isValidCode) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = if (isValidCode) "Verify Code" else "Enter Code",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isValidCode) Color.Black else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                    if (isValidCode) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "→",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Resend Code
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Didn't receive the code? ",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (canResend) {
                Text(
                    text = "Resend",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = LooprCyan,
                    modifier = Modifier.clickable {
                        onResendCode()
                        countdown = 60
                        canResend = false
                    }
                )
            } else {
                Text(
                    text = "Resend in ${countdown}s",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun CodeInputField(
    code: String,
    onCodeChange: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Auto-focus when the composable is first created
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box {
        // Visible digit boxes
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    focusRequester.requestFocus()
                    keyboardController?.show()
                }
        ) {
            repeat(6) { index ->
                CodeDigitBox(
                    digit = code.getOrNull(index)?.toString() ?: "",
                    isFocused = index == code.length,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            focusRequester.requestFocus()
                            keyboardController?.show()
                        }
                )
            }
        }

        // Invisible text field that captures input and handles keyboard
        BasicTextField(
            value = code,
            onValueChange = { newValue ->
                if (newValue.length <= 6 && newValue.all { it.isDigit() }) {
                    onCodeChange(newValue)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(0.dp) // Make it invisible but still functional
                .focusRequester(focusRequester)
        )
    }
}

@Composable
private fun CodeDigitBox(
    digit: String,
    isFocused: Boolean,
    modifier: Modifier = Modifier
) {
    val borderColor = when {
        digit.isNotEmpty() -> LooprCyan
        isFocused -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline
    }

    val backgroundColor = when {
        digit.isNotEmpty() -> LooprCyan.copy(alpha = 0.1f)
        isFocused -> MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
        else -> MaterialTheme.colorScheme.surface
    }

    Box(
        modifier = modifier
            .aspectRatio(1f) // This ensures square boxes
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = digit,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = if (digit.isNotEmpty()) LooprCyan else MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}