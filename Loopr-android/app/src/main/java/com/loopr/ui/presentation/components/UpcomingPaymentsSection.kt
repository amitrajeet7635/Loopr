package com.loopr.ui.presentation.components

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import com.loopr.data.model.Subscription
import com.loopr.ui.theme.LooprDarkCyan
import kotlinx.serialization.json.Json
import java.nio.file.WatchEvent
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Composable
fun UpcomingPaymentsSection() {
    val context = LocalContext.current
    var upcomingSubscriptions by remember { mutableStateOf<List<Subscription>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            upcomingSubscriptions = loadUpcomingSubscriptions(context)
            errorMessage = null
        } catch (e: Exception) {
            errorMessage = e.message
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row (horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()){
            Text(
                text = "Upcoming Payments",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = "within 15days",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = LooprDarkCyan,
                modifier = Modifier.padding(bottom = 12.dp)
            )

        }

        when {
            isLoading -> {
                // Loading state
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Loading payments...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            errorMessage != null -> {
                // Error state
                Card(
                    modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = "Error loading payments: $errorMessage",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            upcomingSubscriptions.isEmpty() -> {
                // Empty state
                Card(
                    modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                ) {
                    Text(
                        text = "No upcoming payments in the next 15 days",
                        modifier = Modifier.padding(24.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            else -> {
                // Content state
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
//                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(upcomingSubscriptions) { subscription ->
                        UpcomingPaymentCard(subscription = subscription)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxHeight()) {
            Text(
                text = "See all subscriptions",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = LooprDarkCyan,
            )
            Icon(
                Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = LooprDarkCyan,
                modifier = Modifier
                    .size(16.dp)
                    .padding(start = 4.dp)
            )
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
private fun UpcomingPaymentCard(subscription: Subscription) {
    val context = LocalContext.current
    val primaryColor = parseColor(subscription.color)
    val secondaryColor =
        Color(ColorUtils.blendARGB(primaryColor.toArgb(), Color.Black.toArgb(), 0.3f))

    val daysUntilDue = calculateDaysUntilDue(subscription.nextDueDate)
    val logoResourceId = getLogoResourceId(context, subscription.logo)


    Card(
        modifier = Modifier
            .width(190.dp)
            .height(160.dp),
        shape = RoundedCornerShape(26.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            primaryColor.copy(alpha = 0.9f), secondaryColor.copy(alpha = 0.7f)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Column(Modifier.fillMaxSize()) {

                // Left side - Logo and service info
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    // Logo
                    if (logoResourceId != 0) {
                        Image(
                            painter = painterResource(id = logoResourceId),
                            contentDescription = "${subscription.merchant} logo",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.9f))
                                .padding(8.dp)
                        )
                    } else {
                        // Fallback icon if logo not found
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White.copy(alpha = 0.9f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = subscription.merchant.first().toString(),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = subscription.merchant,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = subscription.plan,
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f),
                            maxLines = 1,
                            overflow = TextOverflow.Visible
                        )
                    }

                }

                // Right side - Amount and due date
                Column {
                    Text(
                        text = "${
                            String.format(
                                "%.2f", subscription.price
                            )
                        } ${getCurrencySymbol(subscription.currency)}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )

                    Text(
                        text = when {
                            daysUntilDue == 0L -> "Due today"
                            daysUntilDue == 1L -> "Due tomorrow"
                            daysUntilDue > 1 -> "Due in $daysUntilDue days"
                            else -> "Overdue"
                        },
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Normal
                    )
                }
            }

        }
    }
}

private suspend fun loadUpcomingSubscriptions(context: Context): List<Subscription> {
    return try {
        val jsonString =
            context.assets.open("subscriptions.json").bufferedReader().use { it.readText() }

        val allSubscriptions = Json.decodeFromString<List<Subscription>>(jsonString)

        val currentDate = LocalDate.now()
        val fifteenDaysFromNow = currentDate.plusDays(15)

        val filtered = allSubscriptions.filter { subscription ->
            val dueDate = LocalDate.parse(subscription.nextDueDate)
            val isUpcoming = !dueDate.isBefore(currentDate) && !dueDate.isAfter(fifteenDaysFromNow)
            isUpcoming
        }.sortedBy { subscription ->
            LocalDate.parse(subscription.nextDueDate)
        }

        filtered
    } catch (e: Exception) {
        throw e
    }
}

private fun calculateDaysUntilDue(dueDateString: String): Long {
    return try {
        val dueDate = LocalDate.parse(dueDateString)
        val currentDate = LocalDate.now()
        ChronoUnit.DAYS.between(currentDate, dueDate)
    } catch (e: Exception) {
        0L
    }
}

private fun parseColor(colorString: String): Color {
    return try {
        Color(android.graphics.Color.parseColor(colorString))
    } catch (e: Exception) {
        Color(0xFF6200EE) // Default color
    }
}

private fun getLogoResourceId(context: Context, logoFileName: String): Int {
    val resourceName = logoFileName.substringBeforeLast(".") // Remove file extension
    return context.resources.getIdentifier(resourceName, "drawable", context.packageName)
}

private fun getCurrencySymbol(currency: String): String {
    return when (currency.uppercase()) {
        "USD" -> "$"
        "EUR" -> "€"
        "GBP" -> "£"
        "JPY" -> "¥"
        "INR" -> "₹"
        "USDC" -> "USDC "
        else -> "$"
    }
}
