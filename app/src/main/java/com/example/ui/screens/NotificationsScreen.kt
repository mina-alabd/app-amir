package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NotificationEntity
import com.example.ui.components.EmptyStateView
import com.example.ui.theme.*

@Composable
fun NotificationsScreen(
    notifications: List<NotificationEntity>,
    unreadCount: Int,
    isArabic: Boolean,
    onMarkRead: (Long) -> Unit,
    onMarkAllRead: () -> Unit,
    onOpenStory: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("notifications_screen")
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (isArabic) "مركز الإشعارات والتنبيهات" else "Notifications Center",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "${notifications.size} ${if (isArabic) "إشعار" else "notifications"} (${unreadCount} ${if (isArabic) "جديد" else "new"})",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (unreadCount > 0) {
                TextButton(onClick = onMarkAllRead) {
                    Text(if (isArabic) "قراءة الكل ✓" else "Mark all read ✓")
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (notifications.isEmpty()) {
            EmptyStateView(
                icon = Icons.Outlined.NotificationsNone,
                title = if (isArabic) "لا توجد إشعارات حالياً" else "No notifications yet",
                subtitle = if (isArabic) "ستظهر هنا إشعارات الفصول الجديدة، المتابعين، وتفاعلات القراء" else "Updates from authors, followers and comments will appear here"
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(notifications) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onMarkRead(item.id)
                                if (item.targetId != null) {
                                    onOpenStory(item.targetId)
                                }
                            },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (item.isRead) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (item.isRead) 1.dp else 3.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Icon Badge
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (item.type) {
                                            "CHAPTER_UPDATE" -> QessaIndigoPrimary
                                            "FOLLOW" -> QessaGoldSecondary
                                            "LIKE" -> QessaRoseTertiary
                                            "ACHIEVEMENT" -> QessaGoldLight
                                            else -> MaterialTheme.colorScheme.primary
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when (item.type) {
                                        "CHAPTER_UPDATE" -> Icons.Default.MenuBook
                                        "FOLLOW" -> Icons.Default.PersonAdd
                                        "LIKE" -> Icons.Default.Favorite
                                        "ACHIEVEMENT" -> Icons.Default.EmojiEvents
                                        else -> Icons.Default.Notifications
                                    },
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.title,
                                    fontWeight = if (item.isRead) FontWeight.SemiBold else FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = item.message,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            if (!item.isRead) {
                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(8.dp)
                                ) {}
                            }
                        }
                    }
                }
            }
        }
    }
}
