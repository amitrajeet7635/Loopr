package com.loopr.app.ui.presentation.components

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.graphics.ColorUtils
import com.loopr.app.data.model.Subscription
import com.loopr.app.ui.theme.LooprDarkCyan
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.abs


@Composable
fun SwappableCardStack(subscriptions: List<Subscription>) {
    if (subscriptions.isEmpty()) return

    var progress by remember { mutableStateOf(0F) }
    val animatedProgress by animateFloatAsState(progress, animationSpec = spring())
    var frontCardIndex by remember { mutableStateOf(0) }
    var focusedCardIndex by remember { mutableStateOf(0) }


    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cardWidth = screenWidth

    // Track drag for each card without modifying Subscription model
    val dragOffsets = remember(subscriptions) {
        mutableStateListOf<Float>().apply {
            addAll(List(subscriptions.size) { 0f })
        }
    }

    val introOffset = remember { Animatable(0f) }
    var hasAnimatedIntro by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (!hasAnimatedIntro) {
            delay(600)
            introOffset.snapTo(0f)
            introOffset.animateTo(-60f, animationSpec = tween(300))
            delay(200)
            introOffset.animateTo(60f, animationSpec = tween(300))
            delay(200)
            introOffset.animateTo(0f, animationSpec = tween(300))
            hasAnimatedIntro = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp), // Adjust if needed
        contentAlignment = Alignment.TopCenter
    ) {
        subscriptions.forEachIndexed { index, subscription ->
            val animatedDrag by animateFloatAsState(dragOffsets[index], spring())
            val depth = index - focusedCardIndex
            val animatedAlpha by animateFloatAsState(
                targetValue = when {
                    depth == 2 -> 0.3f
                    depth >= 3 -> 0f
                    else -> 1f
                }, animationSpec = tween(500)
            )

            Box(modifier = Modifier
                .zIndex(100f - depth)
                .graphicsLayer {
                    val defaultScale = 1f - (0.1f * index)
                    val isSwapped = index < focusedCardIndex
                    val scaleIncrement = if (isSwapped) 0.1f * index else 0.1f * animatedProgress
                    val scale = defaultScale + scaleIncrement
                    val scaleDownBy = 1f - scale

                    scaleX = scale
                    scaleY = scale

                    val extraTranslationByScale = (180 * scaleDownBy).dp.toPx() / 2f
                    val maxVerticalTranslation = 8.dp * index - (8.dp * animatedProgress)
                    translationY = -(maxVerticalTranslation.toPx() + extraTranslationByScale)
                    translationX = animatedDrag + if (index == 0 && !hasAnimatedIntro) introOffset.value else 0f

                    alpha = animatedAlpha
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            val drag = dragOffsets[focusedCardIndex]
                            val isPullingPrevCard = focusedCardIndex != index
                            val threshold = if (isPullingPrevCard) 0.9f else 0.2f

                            if (abs(drag) / cardWidth.toPx() > threshold) {
                                if (drag > 0f) {
                                    if (focusedCardIndex == 0) return@detectDragGestures

                                    frontCardIndex = (focusedCardIndex - 1).coerceAtLeast(0)
                                    dragOffsets[focusedCardIndex] =
                                        ((screenWidth - cardWidth) / 2 + cardWidth).toPx()
                                } else {
                                    if (focusedCardIndex == subscriptions.lastIndex) return@detectDragGestures

                                    frontCardIndex =
                                        (focusedCardIndex + 1).coerceAtMost(subscriptions.lastIndex)
                                    dragOffsets[focusedCardIndex] =
                                        -((screenWidth - cardWidth) / 2 + cardWidth).toPx()
                                }
                                progress = frontCardIndex.toFloat()
                                focusedCardIndex = frontCardIndex
                            } else {
                                dragOffsets[focusedCardIndex] = 0f
                                progress = focusedCardIndex.toFloat()
                            }
                        }) { _, dragAmount ->
                        val isPullingPrevCard = focusedCardIndex != index

                        if (dragAmount.x < 0f) {
                            if (index == subscriptions.lastIndex) return@detectDragGestures
                            dragOffsets[index] += dragAmount.x
                        } else {
                            if (index == 0) return@detectDragGestures
                            val shouldFocusToPrevCard = dragOffsets[index] >= 0f
                            if (shouldFocusToPrevCard && index > 0) {
                                dragOffsets[index - 1] += dragAmount.x
                                focusedCardIndex = index - 1
                            } else {
                                dragOffsets[index] += dragAmount.x
                            }
                        }

                        progress =
                            -dragOffsets[index] / cardWidth.toPx() + if (isPullingPrevCard) index - 1 else index
                    }
                }) {
                UpcomingPaymentCard(subscription = subscription)
            }
        }
    }
}


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
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
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
                        textAlign = TextAlign.Center
                    )
                }
            }

            else -> {
                // Content state
                SwappableCardStack(subscriptions = upcomingSubscriptions)
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
        Color(ColorUtils.blendARGB(primaryColor.toArgb(), Color.Black.toArgb(), 0.6f))

    val daysUntilDue = calculateDaysUntilDue(subscription.nextDueDate)
    val logoResourceId = getLogoResourceId(context, subscription.logo)


    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(160.dp),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            primaryColor.copy(alpha = 1f), secondaryColor.copy(alpha = 0.8f)
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
                Row(horizontalArrangement = Arrangement.End) {
                    Text(
                        text = "${
                            String.format(
                                "%.2f", subscription.price
                            )
                        } ${getCurrencySymbol(subscription.currency)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                // Right side - Amount and due date
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
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


