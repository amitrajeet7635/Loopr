package com.loopr.ui.presentation.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import coil3.compose.AsyncImage
import com.loopr.ui.presentation.viewmodel.AuthViewModel
import com.loopr.ui.theme.LooprCyan
import com.loopr.ui.theme.LooprCyanVariant

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            LooprTopAppBar(authViewModel = authViewModel)
        },
        bottomBar = {
            LooprBottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        },
        contentWindowInsets = WindowInsets(0) // Remove default content insets to prevent double padding
    ) { paddingValues ->
        // Main content based on selected tab
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                        )
                    )
                )
        ) {
            when (selectedTab) {
                0 -> HomeContent()
                1 -> SubscriptionsContent()
                2 -> WalletContent()
                3 -> RewardsContent()
            }
        }
    }
}

@Composable
private fun LooprBottomNavigationBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val navigationItems = listOf(
        BottomNavItem("Home", Icons.Outlined.Home, Icons.Filled.Home),
        BottomNavItem("Subscriptions", Icons.Outlined.Subscriptions, Icons.Filled.Subscriptions),
        BottomNavItem("Wallet", Icons.Outlined.AccountBalanceWallet, Icons.Filled.AccountBalanceWallet),
        BottomNavItem("Rewards", Icons.Outlined.CardGiftcard, Icons.Filled.CardGiftcard)
    )

    // Main navbar container with proper spacing and FAB integration
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp) // Increased height for better proportions
    ) {
        // Background navbar surface
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(86.dp)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.98f),
            shadowElevation = 32.dp,
            tonalElevation = 16.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.98f),
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                            )
                        ),
                        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left section: Home and Subscriptions
                    Row(
                        modifier = Modifier.weight(2f),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        NavBarItem(
                            item = navigationItems[0], // Home
                            isSelected = selectedTab == 0,
                            onClick = { onTabSelected(0) },
                            modifier = Modifier.weight(1f)
                        )

                        NavBarItem(
                            item = navigationItems[1], // Subscriptions
                            isSelected = selectedTab == 1,
                            onClick = { onTabSelected(1) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Center: Space for FAB with proper visual integration
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Subtle cutout effect for FAB
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .background(
                                    Color.Transparent,
                                    CircleShape
                                )
                        )
                    }

                    // Right section: Wallet and Rewards
                    Row(
                        modifier = Modifier.weight(2f),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        NavBarItem(
                            item = navigationItems[2], // Wallet
                            isSelected = selectedTab == 2,
                            onClick = { onTabSelected(2) },
                            modifier = Modifier.weight(1f)
                        )

                        NavBarItem(
                            item = navigationItems[3], // Rewards
                            isSelected = selectedTab == 3,
                            onClick = { onTabSelected(3) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // FAB positioned perfectly in the center cutout
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 4.dp) // Moved higher up for better positioning
        ) {
            // Enhanced FAB with better integration
            FloatingActionButton(
                onClick = { /* TODO: Implement QR scan */ },
                modifier = Modifier.size(68.dp),
                shape = CircleShape,
                containerColor = LooprCyan,
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 16.dp,
                    pressedElevation = 20.dp,
                    hoveredElevation = 18.dp
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    LooprCyan,
                                    LooprCyanVariant,
                                    LooprCyan.copy(alpha = 0.9f)
                                )
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.QrCodeScanner,
                        contentDescription = "Scan QR",
                        modifier = Modifier.size(32.dp),
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeContent() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Welcome Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Welcome back! 👋",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Manage your subscriptions effortlessly",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            // Quick Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Active",
                    value = "12",
                    subtitle = "Subscriptions",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Monthly",
                    value = "$89",
                    subtitle = "Total Cost",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            // Recent Activity
            Text(
                text = "Recent Activity",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }

        items(3) { index ->
            ActivityCard(
                title = when(index) {
                    0 -> "Netflix"
                    1 -> "Spotify Premium"
                    else -> "Adobe Creative"
                },
                amount = when(index) {
                    0 -> "$15.99"
                    1 -> "$9.99"
                    else -> "$52.99"
                },
                date = when(index) {
                    0 -> "Today"
                    1 -> "Yesterday"
                    else -> "2 days ago"
                }
            )
        }
    }
}

@Composable
private fun SubscriptionsContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.Subscriptions,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = LooprCyan
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Subscriptions",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Coming Soon",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun WalletContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.AccountBalanceWallet,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = LooprCyan
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Wallet",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Coming Soon",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun RewardsContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.CardGiftcard,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = LooprCyan
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Rewards",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Coming Soon",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = LooprCyan.copy(alpha = 0.05f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = LooprCyan
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ActivityCard(
    title: String,
    amount: String,
    date: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = amount,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = LooprCyan
            )
        }
    }
}

@Composable
private fun NavBarItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        animationSpec = tween(200, easing = FastOutSlowInEasing),
        label = "scale"
    )

    val animatedAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.6f,
        animationSpec = tween(200),
        label = "alpha"
    )

    Box(
        modifier = modifier
            .height(56.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // Removed the circular background highlight structure

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .graphicsLayer {
                    scaleX = animatedScale
                    scaleY = animatedScale
                    alpha = animatedAlpha
                }
        ) {
            Icon(
                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                contentDescription = item.label,
                tint = if (isSelected) LooprCyan else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = item.label,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isSelected) LooprCyan else MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private data class BottomNavItem(
    val label: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector
)

@Composable
private fun LooprTopAppBar(
    authViewModel: AuthViewModel
) {
    var showDropdown by remember { mutableStateOf(false) }

    // Collect user profile data from AuthViewModel
    val userProfile by authViewModel.userProfile.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding(), // Add status bar padding to prevent overlap
        color = MaterialTheme.colorScheme.background,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Loopr Logo/Brand
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo placeholder - will be replaced with actual logo later
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(LooprCyan, LooprCyanVariant)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "L",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Loopr",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // Account Avatar with Dropdown
            Box {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    LooprCyan.copy(alpha = 0.2f),
                                    LooprCyan.copy(alpha = 0.1f)
                                )
                            )
                        )
                        .clickable { showDropdown = !showDropdown },
                    contentAlignment = Alignment.Center
                ) {
                    // AsyncImage to load and display the actual profile image from datastore
                    if (userProfile.profileImageUrl.isNotEmpty()) {
                        var isLoading by remember { mutableStateOf(true) }
                        var hasError by remember { mutableStateOf(false) }

                        AsyncImage(
                            model = userProfile.profileImageUrl,
                            contentDescription = "Account",
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            onLoading = { isLoading = true; hasError = false },
                            onSuccess = { isLoading = false; hasError = false },
                            onError = { isLoading = false; hasError = true }
                        )

                        if (isLoading || hasError) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = "Account",
                                tint = LooprCyan,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    } else {
                        // Show default icon when no profile image URL is available
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Account",
                            tint = LooprCyan,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Dropdown Menu with real user data
                DropdownMenu(
                    expanded = showDropdown,
                    onDismissRequest = { showDropdown = false },
                    modifier = Modifier.width(200.dp),
                    properties = PopupProperties(
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true
                    )
                ) {
                    // Account Name - Now using real data from DataStore
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(
                                    text = userProfile.name.ifEmpty { "User" }, // Use real name from DataStore
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = userProfile.emailId.ifEmpty { "No email" }, // Use real email from DataStore
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        onClick = { },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.AccountCircle,
                                contentDescription = null,
                                tint = LooprCyan
                            )
                        }
                    )

                    HorizontalDivider()

                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Profile",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        onClick = {
                            showDropdown = false
                            // TODO: Navigate to profile
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )

                    // Logout Option
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Logout",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error
                            )
                        },
                        onClick = {
                            showDropdown = false
                            // TODO: Handle logout functionality
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.ExitToApp,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    )
                }
            }
        }
    }
}
