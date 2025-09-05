package com.loopr.app.ui.presentation.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.material.icons.outlined.TrendingDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import coil3.compose.AsyncImage
import com.loopr.app.ui.presentation.components.UpcomingPaymentsSection
import com.loopr.app.ui.theme.LooprCyan
import com.loopr.app.ui.theme.LooprCyanVariant
import com.web3auth.core.types.UserInfo

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    userInfo: UserInfo?,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            LooprBottomNavigationBar(
                selectedTab = selectedTab, onTabSelected = { selectedTab = it })
        },
        contentWindowInsets = WindowInsets(0)
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
                0 -> HomeContent(userInfo = userInfo, onLogout = onLogout)
                1 -> SubscriptionsScreen()
                2 -> WalletContent()
                3 -> RewardsContent()
            }
        }
    }
}


@Composable
private fun LooprBottomNavigationBar(
    selectedTab: Int, onTabSelected: (Int) -> Unit
) {
    val navigationItems = listOf(
        BottomNavItem("Home", Icons.Outlined.Home, Icons.Filled.Home),
        BottomNavItem("Subscriptions", Icons.Outlined.Subscriptions, Icons.Filled.Subscriptions),
        BottomNavItem(
            "Wallet", Icons.Outlined.AccountBalanceWallet, Icons.Filled.AccountBalanceWallet
        ),
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
                        ), shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
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
                                    Color.Transparent, CircleShape
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
                    defaultElevation = 16.dp, pressedElevation = 20.dp, hoveredElevation = 18.dp
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    LooprCyan, LooprCyanVariant, LooprCyan.copy(alpha = 0.9f)
                                )
                            ), shape = CircleShape
                        ), contentAlignment = Alignment.Center
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
private fun HomeContent(userInfo: UserInfo?, onLogout: () -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp))
    {
        item {
            LooprTopAppBar(userInfo = userInfo, onLogout = onLogout)
        }

        item {
            // Overview Card - Hero Section
            OverviewCard()
        }

        item {
            // Upcoming Payments Section
            UpcomingPaymentsSection()
        }
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
private fun NavBarItem(
    item: BottomNavItem, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier
) {
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        animationSpec = tween(200, easing = FastOutSlowInEasing),
        label = "scale"
    )

    val animatedAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.6f, animationSpec = tween(200), label = "alpha"
    )

    Box(
        modifier = modifier
            .height(56.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() }, indication = null
            ) { onClick() }, contentAlignment = Alignment.Center
    ) {
        // Removed the circular background highlight structure

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.graphicsLayer {
                scaleX = animatedScale
                scaleY = animatedScale
                alpha = animatedAlpha
            }) {
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
    val label: String, val unselectedIcon: ImageVector, val selectedIcon: ImageVector
)

@Composable
private fun LooprTopAppBar(
    userInfo: UserInfo?,
    onLogout: () -> Unit
) {
    var showDropdown by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxWidth(),
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
                    ), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "L", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White
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
                                LooprCyan.copy(alpha = 0.2f), LooprCyan.copy(alpha = 0.1f)
                            )
                        )
                    )
                    .clickable { showDropdown = !showDropdown },
                contentAlignment = Alignment.Center
            ) {
                if (!userInfo?.profileImage.isNullOrEmpty()) {
                    var isLoading by remember { mutableStateOf(true) }
                    var hasError by remember { mutableStateOf(false) }

                    AsyncImage(
                        model = userInfo?.profileImage,
                        contentDescription = "Account",
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        onLoading = { isLoading = true; hasError = false },
                        onSuccess = { isLoading = false; hasError = false },
                        onError = { isLoading = false; hasError = true })

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
                    dismissOnBackPress = true, dismissOnClickOutside = true
                )
            ) {
                // Account Name - Now using real data directly from Web3Auth state
                DropdownMenuItem(text = {
                    Column {
                        Text(
                            text = userInfo?.name ?: "User",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = userInfo?.email ?: "No email",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }, onClick = { }, leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = null,
                        tint = LooprCyan
                    )
                })

                HorizontalDivider()

                DropdownMenuItem(text = {
                    Text(
                        text = "Profile", style = MaterialTheme.typography.bodyLarge
                    )
                }, onClick = {
                    showDropdown = false
                    // TODO: Navigate to profile
                }, leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                })

                // Logout Option
                DropdownMenuItem(text = {
                    Text(
                        text = "Logout",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }, onClick = {
                    showDropdown = false
                    onLogout()
                }, leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.ExitToApp,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                })
            }
        }
    }
}


@Composable
private fun OverviewCard() {
    // Sample data - replace with actual data from your ViewModel/Repository
    val totalSpent = 3200f
    val monthlyBudget = 4000f
    val isOverBudget = totalSpent > monthlyBudget
    (totalSpent / monthlyBudget).coerceAtMost(1.2f) // Cap at 120% for visual purposes
    val overAmount = if (isOverBudget) totalSpent - monthlyBudget else monthlyBudget - totalSpent
    ((overAmount / monthlyBudget) * 100).toInt()

    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(28.dp))
            .glassEffect(28.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.05f)
                        )
                    )
                )
                .padding(32.dp)
        ) {
            // Header Section - Loopr branded
            Column {
                Text(
                    text = "Here's your monthly spent on Loopr",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.inverseSurface,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "${String.format("%.2f", totalSpent)}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = if (isOverBudget) MaterialTheme.colorScheme.error else LooprCyan
                    )
                    Text(
                        text = "USDC",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isOverBudget) MaterialTheme.colorScheme.error else LooprCyan
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(
                color = Color(0xFFFFFFFF),
                trackColor = Color(0xFF7A89D8),
                progress = { 0.53f },
                strokeCap = StrokeCap.Square,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50.dp))
                    .height(12.dp),
                drawStopIndicator = {})

            Text(
                "You have spent ~53.81%",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.inverseSurface,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            // Budget status - only show when over budget
            if (isOverBudget) {
                Spacer(modifier = Modifier.height(16.dp))
                //BudgetCrossed with Icon high rising
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccountBalanceWallet,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "You are over your budget by ${
                            String.format(
                                "%.2f", overAmount
                            )
                        } USDC",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                // Show like Spending stats - 5% less than prev month with Icon
                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Icon(
                        Icons.Outlined.TrendingDown,
                        contentDescription = null,
                        tint = LooprCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "5% less than prev month",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                }

            }

        }
    }
}

fun Modifier.glassEffect(roundedCorner: Dp) = this
    .background(
        brush = Brush.verticalGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.25f), Color.White.copy(alpha = 0.15f)
            )
        )
    )
    .border(
        width = 1.dp, brush = Brush.verticalGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.4f), Color.White.copy(alpha = 0.1f)
            )
        ), shape = RoundedCornerShape(roundedCorner)
    )
