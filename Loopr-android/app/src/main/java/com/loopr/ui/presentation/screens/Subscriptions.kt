package com.loopr.ui.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.loopr.data.model.Subscription
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

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
                // Title section - positioned at top with more spacing
                Text(
                    "Subscriptions",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 24.dp)
                )

                // Pills section - positioned at bottom of colored area
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

        // Horizontal Pager
        HorizontalPager(
            state = pagerState, modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> {
                    // Scrollable subscription cards like in the image
                    val subscriptions = loadSubscriptionsFromAssets()

                    if (subscriptions.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp), // Small gap between cards
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            itemsIndexed(subscriptions) { index, subscription ->
                                FullSubscriptionCard(
                                    subscription = subscription,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp) // Each card takes significant screen space
                                )
                            }
                        }
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
                    // Analytics content
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

// Helper function to load subscriptions from assets
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

// Individual subscription card for proper stacking
@Composable
fun SubscriptionCard(
    subscription: Subscription, modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp) // Full card height like in the image
            .padding(horizontal = 16.dp)
            .shadow(
                elevation = 8.dp, shape = RoundedCornerShape(20.dp)
            )
            .clip(RoundedCornerShape(20.dp))
            .background(Color(android.graphics.Color.parseColor(subscription.color)))
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top section with service info
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                // Service icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = getServiceIcon(subscription.merchant),
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Service name and plan
                Column {
                    Text(
                        text = subscription.merchant,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = subscription.plan,
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            // Bottom section with pricing
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "4 Weeks",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "$${subscription.price}",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Helper function to get service icons
fun getServiceIcon(serviceName: String): String {
    return when (serviceName.lowercase()) {
        "netflix" -> "N"
        "amazon prime video" -> "a"
        "disney+" -> "D"
        "spotify" -> "♪"
        "hbo max" -> "H"
        else -> serviceName.first().toString()
    }
}

// Properly stacked subscription cards with scrolling
@Composable
fun SubscriptionStack(
    subscriptions: List<Subscription>, modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp), // Small gap between cards
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        itemsIndexed(subscriptions) { index, subscription ->
            SubscriptionCard(
                subscription = subscription, modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// Modern animated subscription card with stacked layout
@Composable
fun AnimatedSubscriptionCard(
    subscription: Subscription,
    index: Int,
    totalCards: Int,
    isVisible: Boolean = true,
    modifier: Modifier = Modifier
) {
    // Animation states
    val animatedElevation by animateDpAsState(
        targetValue = if (isVisible) (8 + index * 2).dp else 0.dp, animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
        ), label = "elevation"
    )

    val animatedOffset by animateDpAsState(
        targetValue = if (isVisible) (index * 8).dp else 0.dp, animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium
        ), label = "offset"
    )

    val animatedScale by animateFloatAsState(
        targetValue = if (isVisible) 1f - (index * 0.02f) else 0.8f, animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium
        ), label = "scale"
    )

    val animatedAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f, animationSpec = tween(300), label = "alpha"
    )

    AnimatedVisibility(
        visible = isVisible, enter = slideInVertically(
            initialOffsetY = { it / 2 }, animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium
            )
        ) + fadeIn(animationSpec = tween(300)), exit = slideOutVertically(
            targetOffsetY = { -it / 2 }, animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium
            )
        ) + fadeOut(animationSpec = tween(300))
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(horizontal = 16.dp)
                .offset(y = animatedOffset)
                .graphicsLayer {
                    scaleX = animatedScale
                    scaleY = animatedScale
                    alpha = animatedAlpha
                }
                .zIndex((totalCards - index).toFloat()),
            colors = CardDefaults.cardColors(
                containerColor = Color(android.graphics.Color.parseColor(subscription.color))
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = animatedElevation),
            shape = RoundedCornerShape(20.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Service logo (left side) - only show if drawable exists
                val logoRes = getServiceLogoRes(subscription)
                if (logoRes != null) {
                    Image(
                        painter = painterResource(id = logoRes),
                        contentDescription = subscription.merchant,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                            .align(Alignment.TopStart)
                            .padding(8.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                // Service name + plan (center-left)
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 64.dp)
                ) {
                    Text(
                        text = subscription.merchant,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = subscription.plan,
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Price (top-right corner)
                Text(
                    text = "$${subscription.price}",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.TopEnd)
                )

                // Frequency (bottom-right corner)
                Text(
                    text = subscription.frequency,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }
    }
}

// Helper function to get service logos resources
fun getServiceLogoRes(subscription: Subscription): Int? {
    // Get the logo filename from JSON (e.g., "netflix.png")
    val logoFileName = subscription.logo

    // Remove the .png extension to get the drawable name
    val drawableName = logoFileName.replace(".png", "").lowercase()

    // Map drawable names to resource IDs directly
    return when (drawableName) {
        "netflix" -> com.loopr.R.drawable.netflix
        "spotify" -> com.loopr.R.drawable.spotify
        "disney" -> com.loopr.R.drawable.disney
        "prime" -> com.loopr.R.drawable.prime
        "hbo" -> com.loopr.R.drawable.hbo
        else -> null // If drawable not found, return null to show nothing
    }
}

// Stacked subscription cards container with animations
@Composable
fun StackedSubscriptionCards(
    subscriptions: List<Subscription>, modifier: Modifier = Modifier
) {
    var visibleCards by remember { mutableStateOf<Set<Int>>(emptySet()) }

    // Animate cards appearing one by one
    LaunchedEffect(subscriptions) {
        subscriptions.forEachIndexed { index, _ ->
            delay(index * 150L) // Stagger animation
            visibleCards = visibleCards + index
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        // Render cards in reverse order for proper stacking
        subscriptions.asReversed().forEachIndexed { reverseIndex, subscription ->
            val actualIndex = subscriptions.size - 1 - reverseIndex
            AnimatedSubscriptionCard(
                subscription = subscription,
                index = reverseIndex,
                totalCards = subscriptions.size,
                isVisible = visibleCards.contains(actualIndex),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// Main subscription section with proper animations
@Composable
fun SubscriptionSection(
    subscriptions: List<Subscription>, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        if (subscriptions.isNotEmpty()) {
            StackedSubscriptionCards(
                subscriptions = subscriptions.take(5), // Limit to 5 cards for better performance
                modifier = Modifier.fillMaxWidth()
            )

            // Show remaining cards count if more than 5
            if (subscriptions.size > 5) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+${subscriptions.size - 5} more subscriptions",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No subscriptions found",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// Simple subscription card without complex animations
@Composable
fun SimpleSubscriptionCard(
    subscription: Subscription,
    index: Int = 0,
    isTopCard: Boolean = true,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color(android.graphics.Color.parseColor(subscription.color))
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = (8 + index * 2).dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (isTopCard) {
                // Full layout for top card (Netflix) - exactly like inspiration
                // Service logo (top-left) - only show if drawable exists
                val logoRes = getServiceLogoRes(subscription)
                if (logoRes != null) {
                    Image(
                        painter = painterResource(id = logoRes),
                        contentDescription = subscription.merchant,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                            .align(Alignment.TopStart)
                            .padding(8.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                // Service name + plan (center-left)
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 64.dp)
                ) {
                    Text(
                        text = subscription.merchant,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = subscription.plan,
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Price (top-right)
                Text(
                    text = "$${subscription.price}",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.TopEnd)
                )

                // Frequency (bottom-right)
                Text(
                    text = subscription.frequency,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            } else {
                // Simplified layout for stacked cards - only show service name prominently
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopStart),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Service logo (smaller for stacked cards) - only show if drawable exists
                    val logoRes = getServiceLogoRes(subscription)
                    if (logoRes != null) {
                        Image(
                            painter = painterResource(id = logoRes),
                            contentDescription = subscription.merchant,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f))
                                .padding(6.dp),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(modifier = Modifier.width(12.dp))
                    }

                    // Service name (prominent for identification)
                    Text(
                        text = subscription.merchant,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Price (small, top-right)
                Text(
                    text = "$${subscription.price}",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }
        }
    }
}

// Full subscription card for scrollable list - using real JSON data
@Composable
fun FullSubscriptionCard(
    subscription: Subscription, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = getServiceColor(subscription.color)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Service logo (top-left) - only show if drawable exists
            val logoRes = getServiceLogoRes(subscription)
            if (logoRes != null) {
                Image(
                    painter = painterResource(id = logoRes),
                    contentDescription = subscription.merchant,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                        .align(Alignment.TopStart)
                        .padding(8.dp),
                    contentScale = ContentScale.Fit
                )
            }

            // Service name + plan (center-left)
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 64.dp)
            ) {
                Text(
                    text = subscription.merchant,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subscription.plan,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Price with USDC currency (top-right)
            Column(
                modifier = Modifier.align(Alignment.TopEnd), horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${subscription.price} ${subscription.currency}",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Days until next payment (bottom-right)
            Text(
                text = calculateDaysUntilNext(subscription.nextDueDate),
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}

// Helper function to calculate days until next payment
fun calculateDaysUntilNext(nextDueDate: String): String {
    return try {
        // Simple calculation - in a real app you'd use proper date parsing
        when {
            nextDueDate.contains("2025-08-28") -> "1 Day"
            nextDueDate.contains("2025-08-30") -> "3 Days"
            nextDueDate.contains("2025-09-01") -> "5 Days"
            nextDueDate.contains("2025-09-05") -> "9 Days"
            nextDueDate.contains("2025-09-07") -> "11 Days"
            else -> "Soon"
        }
    } catch (e: Exception) {
        "Soon"
    }
}

// Helper function to get shaded brand colors
fun getServiceColor(baseColor: String): Color {
    return try {
        Color(android.graphics.Color.parseColor(baseColor))
    } catch (e: Exception) {
        Color(0xFF6200EE) // Fallback color
    }
}
