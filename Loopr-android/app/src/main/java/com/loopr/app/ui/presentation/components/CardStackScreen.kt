package com.loopr.app.ui.presentation.components

import android.graphics.Color.parseColor
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.graphics.ColorUtils
import com.loopr.app.data.model.Subscription
import com.loopr.app.ui.theme.ErrorColor
import kotlinx.coroutines.launch

@Composable
fun CardStackScreen(oriList: List<Subscription>) {
    val curList = remember {
        mutableStateListOf<Subscription>().apply {
            addAll(oriList.map { it.copy(selected = false, collapsed = true) })
        }
    }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    CardStack(
        items = curList,
        collapsedTop = curList.firstOrNull()?.collapsed ?: false,
        onItemClick = { item ->
            when (item.selected) {
                false -> {
                    // Select new item
                    val newList = oriList.toMutableList().apply {
                        removeIf { it.merchant == item.merchant }
                        add(0, item.copy(selected = true, collapsed = false))
                    }
                    curList.clear(); curList.addAll(newList)
                    scope.launch { listState.animateScrollToItem(0) }
                }

                true -> {
                    // Toggle collapse of existing top item
                    val newList = curList.toMutableList().apply {
                        removeIf { it.merchant == item.merchant }
                        add(0, item.copy(selected = true, collapsed = !item.collapsed))
                    }
                    curList.clear(); curList.addAll(newList)
                    scope.launch { listState.animateScrollToItem(0) }
                }
            }
        },
        listState = listState,
        stackGap = 60.dp,
        cardHeight = 160.dp
    )
}


@Composable
fun CardStack(
    items: List<Subscription>,
    collapsedTop: Boolean,
    onItemClick: (Subscription) -> Unit,
    listState: LazyListState,
    cardHeight: Dp,
    stackGap: Dp
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        state = listState,
        contentPadding = PaddingValues(
            0.dp// mimic outRect.bottom = 600px
        )
    ) {
        itemsIndexed(items, key = { _, item -> item.merchant }) { index, item ->
            // Calculate target height based on card state
            val targetHeight = when {
                index == 0 -> if (item.collapsed) cardHeight else cardHeight * 1.3f // Top card can expand
                else -> cardHeight // Other cards maintain normal height
            }

            val animatedHeight by animateDpAsState(
                targetValue = targetHeight,
                animationSpec = spring(stiffness = Spring.StiffnessLow),
                label = "heightAnimation"
            )

            // Calculate stacking offset - only for non-top cards when top is collapsed
            val targetOffset = when {
                index == 0 -> 0.dp
                item.selected && !item.collapsed -> 0.dp
                collapsedTop -> -stackGap * index
                items.firstOrNull()?.selected == true && items.firstOrNull()?.collapsed == true -> -stackGap * index
                else -> 0.dp
            }

            val animatedOffset by animateDpAsState(
                targetValue = targetOffset,
                animationSpec = spring(stiffness = Spring.StiffnessMedium),
                label = "offsetAnimation"
            )

            val zIndex = (index.toFloat())

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(animatedHeight)
                    .offset(y = animatedOffset)
                    .zIndex(zIndex)
                    .padding(
                        horizontal = 16.dp, vertical = if (index > 0 && collapsedTop) 0.dp else 8.dp
                    )
                    .clickable { onItemClick(item) }) {
                SubscriptionCard(
                    item = item,
                    isTop = index == 0,
                    isExpanded = !item.collapsed,
                    collapsedTop = collapsedTop
                )
            }
        }
    }

    // Scroll to top when a new card is selected
    LaunchedEffect(items.firstOrNull()?.merchant) {
        if (items.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }
}


@Composable
fun SubscriptionCard(
    item: Subscription, isTop: Boolean, isExpanded: Boolean, collapsedTop: Boolean
) {
    val primaryColor = parseColour(item.color)
    val secondaryColor =
        Color(ColorUtils.blendARGB(primaryColor.toArgb(), Color.Black.toArgb(), 0.6f))

    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            primaryColor.copy(alpha = 1f), secondaryColor.copy(alpha = 0.9f)
        )

    )

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp, focusedElevation = 10.dp, pressedElevation = 10.dp
        ),
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .background(gradientBrush)
                .padding(16.dp)
        ) {
            // Logo
            val logoPainter = painterResource(id = getDrawableId(item.logo))


            // Text details - show more info when expanded
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row {
                        Image(
                            painter = logoPainter,
                            contentDescription = "${item.merchant} logo",
                            modifier = Modifier
                                .padding(end = 6.dp)
                                .size(40.dp)
                                .size(50.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.5f))
                                .padding(6.dp)

                        )
                        Column {
                            Text(
                                text = item.merchant,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )


                            Text(
                                text = "${item.plan}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White
                            )
                        }

                    }

                    Row {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${item.price} ${item.currency}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Text(
                                text = "${item.frequency} ",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White
                            )
                        }
                    }

                }


                if (isExpanded && isTop) {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "Next due payment: ${item.nextDueDate}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White.copy(alpha = 0.9f)
                    )

                    Spacer(Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { /* TODO: Handle action */ },
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = ErrorColor, contentColor = Color.Black
                            )
                        ) {

                            Icon(
                                Icons.Default.DeleteForever,
                                contentDescription = "Delete",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(end = 2.dp)
                            )
                            Text(
                                text = "Cancel",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )

                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Button(
                            onClick = { /* TODO: Handle action */ },
                            colors = ButtonDefaults.elevatedButtonColors()
                        ) {
                            Icon(
                                Icons.Default.Pause,
                                contentDescription = "Delete",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(end = 2.dp)
                            )
                            Text(text = "Pause", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}


private fun parseColour(colorString: String): Color {
    return try {
        Color(parseColor(colorString))
    } catch (e: Exception) {
        Color(0xFF6200EE) // Default color
    }
}


@Composable
fun getDrawableId(fileName: String): Int {
    val cleanName = fileName.substringBeforeLast(".")
    return LocalContext.current.resources.getIdentifier(
        cleanName, "drawable", LocalContext.current.packageName
    )
}