package com.loopr.ui.presentation.screens

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SubscriptionsScreen(modifier: Modifier = Modifier) {
    Surface(modifier = Modifier.fillMaxSize()) {
        // Content for Subscriptions Screen
        Box(
            modifier = Modifier
                .fillMaxHeight(0.3f)  // 30% of parent's height
                .fillMaxWidth()       // full width
                .background(Color.Red)
        )
    }
}