package com.loopr.ui.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.loopr.data.model.Subscription
import com.loopr.ui.presentation.components.CardStackScreen
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

/**
NEW ANIMATION
 */


@Composable
fun SubscriptionsScreen(modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxSize()) {
        // Header section with colored background
        Box(
            modifier = Modifier
                .background(Color(0xFFB4C4E7))
                .fillMaxWidth()
                .fillMaxHeight(0.30f)
        ) {
            Column(
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Title section
                Text(
                    "Subscriptions",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 24.dp)
                )

                // Pills row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 18.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Subscriptions pill
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(28.dp))
                            .background(
                                if (pagerState.currentPage == 0) Color(0xFF2E2E2E)
                                else Color(0xFF4A4A4A).copy(alpha = 0.7f)
                            )
                            .clickable {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(0)
                                }
                            }
                            .padding(vertical = 10.dp, horizontal = 16.dp),
                        contentAlignment = Alignment.Center) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Subscriptions,
                                contentDescription = "Subscriptions",
                                tint = if (pagerState.currentPage == 0) Color.White
                                else Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Subscriptions",
                                color = if (pagerState.currentPage == 0) Color.White
                                else Color.White.copy(alpha = 0.8f),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (pagerState.currentPage == 0) FontWeight.SemiBold
                                else FontWeight.Medium
                            )
                        }
                    }

                    // Analytics pill
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(28.dp))
                            .background(
                                if (pagerState.currentPage == 1) Color(0xFF2E2E2E)
                                else Color(0xFF4A4A4A).copy(alpha = 0.7f)
                            )
                            .clickable {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(1)
                                }
                            }
                            .padding(vertical = 10.dp, horizontal = 16.dp),
                        contentAlignment = Alignment.Center) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Analytics,
                                contentDescription = "Analytics",
                                tint = if (pagerState.currentPage == 1) Color.White
                                else Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Analytics",
                                color = if (pagerState.currentPage == 1) Color.White
                                else Color.White.copy(alpha = 0.8f),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (pagerState.currentPage == 1) FontWeight.SemiBold
                                else FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // Horizontal Pager for pages
        HorizontalPager(
            state = pagerState, modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> {
                    // ✅ Replace old cards with your new CardStackScreen
                    val subscriptions = loadSubscriptionsFromAssets()
                    if (subscriptions.isNotEmpty()) {
                        CardStackScreen(oriList = subscriptions)
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No subscriptions found",
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color.Gray
                            )
                        }
                    }
                }

                1 -> {
                    // Analytics page placeholder
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Analytics Content",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.Black,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun loadSubscriptionsFromAssets(): List<Subscription> {
    val context = LocalContext.current
    return remember {
        try {
            val jsonString =
                context.assets.open("subscriptions.json").bufferedReader().use { it.readText() }
            Json.decodeFromString<List<Subscription>>(jsonString)
        } catch (_: Exception) {
            emptyList()
        }
    }
}

