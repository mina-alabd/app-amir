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
import com.example.data.model.ReportEntity
import com.example.data.model.StoryEntity
import com.example.data.model.UserEntity
import com.example.ui.components.EmptyStateView
import com.example.ui.components.StatCard
import com.example.ui.theme.*

@Composable
fun AdminDashboardScreen(
    currentUser: UserEntity?,
    allUsers: List<UserEntity>,
    allStories: List<StoryEntity>,
    allReports: List<ReportEntity>,
    totalStoriesCount: Int,
    totalChaptersCount: Int,
    totalCommentsCount: Int,
    totalReads: Int,
    isArabic: Boolean,
    onToggleUserBan: (UserEntity) -> Unit,
    onChangeUserRole: (Long, String) -> Unit,
    onToggleStoryFeatured: (StoryEntity) -> Unit,
    onDeleteStory: (Long) -> Unit,
    onResolveReport: (Long, String) -> Unit,
    onOpenStory: (Long) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = if (isArabic) {
        listOf("الإحصائيات", "البلاغات (${allReports.count { it.status == "PENDING" }})", "إدارة القصص", "المستخدمون")
    } else {
        listOf("Stats", "Reports (${allReports.count { it.status == "PENDING" }})", "Stories", "Users")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("admin_dashboard_screen")
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Title Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AdminPanelSettings,
                contentDescription = null,
                tint = QessaGoldSecondary,
                modifier = Modifier.size(28.dp)
            )
            Column {
                Text(
                    text = if (isArabic) "لوحة تحكم الإدارة والإشراف" else "Admin & Moderation Hub",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = if (isArabic) "إدارة المحتوى، معالجة البلاغات، ومراقبة نشاط المنصة" else "Manage content, resolve reports, and oversee platform activity",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Navigation Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            edgePadding = 0.dp,
            divider = {}
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedTab) {
            0 -> {
                // Overview & Stats
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            StatCard(
                                title = if (isArabic) "إجمالي القصص" else "Total Stories",
                                value = totalStoriesCount.toString(),
                                icon = Icons.Default.MenuBook,
                                iconColor = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                title = if (isArabic) "إجمالي الفصول" else "Chapters",
                                value = totalChaptersCount.toString(),
                                icon = Icons.Default.Article,
                                iconColor = QessaGoldSecondary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            StatCard(
                                title = if (isArabic) "إجمالي القراءات" else "Total Reads",
                                value = totalReads.toString(),
                                icon = Icons.Default.Visibility,
                                iconColor = QessaRoseTertiary,
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                title = if (isArabic) "إجمالي التعليقات" else "Comments",
                                value = totalCommentsCount.toString(),
                                icon = Icons.Default.ChatBubbleOutline,
                                iconColor = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = if (isArabic) "حالة المنصة وموجز الأمان" else "Platform & Security Status",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = if (isArabic) "• إجمالي الحسابات النشطة: ${allUsers.size}\n• البلاغات المعلقة: ${allReports.count { it.status == "PENDING" }}\n• القصص المميزة الحالية: ${allStories.count { it.isFeatured }}"
                                    else "• Active Accounts: ${allUsers.size}\n• Pending Reports: ${allReports.count { it.status == "PENDING" }}\n• Featured Stories: ${allStories.count { it.isFeatured }}",
                                    fontSize = 13.sp,
                                    lineHeight = 20.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
            1 -> {
                // Reports Queue
                if (allReports.isEmpty()) {
                    EmptyStateView(
                        icon = Icons.Outlined.CheckCircle,
                        title = if (isArabic) "لا توجد بلاغات حالياً" else "No pending reports",
                        subtitle = if (isArabic) "سجل البلاغات نظيف، جميع المحتويات متوافقة مع إرشادات المنصة." else "All content adheres to community guidelines."
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(allReports, key = { it.id }) { report ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (report.status == "PENDING") MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                                )
                            ) {
                                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = if (report.status == "PENDING") QessaRoseTertiary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.primaryContainer,
                                            contentColor = if (report.status == "PENDING") QessaRoseTertiary else MaterialTheme.colorScheme.primary
                                        ) {
                                            Text(
                                                text = "${report.targetType}: ${report.targetTitle}",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                            )
                                        }

                                        Text(
                                            text = report.status,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (report.status == "PENDING") QessaGoldSecondary else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Text(
                                        text = "${if (isArabic) "المُبلّغ:" else "Reporter:"} ${report.reporterName} • ${if (isArabic) "السبب:" else "Reason:"} ${report.reason}",
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    if (report.status == "PENDING") {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            OutlinedButton(
                                                onClick = { onResolveReport(report.id, "DISMISSED") },
                                                shape = RoundedCornerShape(8.dp)
                                            ) {
                                                Text(if (isArabic) "تجاهل" else "Dismiss", fontSize = 12.sp)
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Button(
                                                onClick = { onResolveReport(report.id, "RESOLVED") },
                                                shape = RoundedCornerShape(8.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                            ) {
                                                Text(if (isArabic) "معالجة واتخاذ إجراء" else "Resolve & Action", fontSize = 12.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            2 -> {
                // Stories Management
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(allStories, key = { it.id }) { story ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = story.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "${if (isArabic) "الكاتب:" else "Author:"} ${story.authorName} • ${story.readsCount} ${if (isArabic) "قراءة" else "reads"}",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                IconButton(onClick = { onToggleStoryFeatured(story) }) {
                                    Icon(
                                        imageVector = if (story.isFeatured) Icons.Filled.Star else Icons.Outlined.StarOutline,
                                        contentDescription = "Feature",
                                        tint = if (story.isFeatured) QessaGoldSecondary else MaterialTheme.colorScheme.outline
                                    )
                                }

                                IconButton(onClick = { onOpenStory(story.id) }) {
                                    Icon(
                                        imageVector = Icons.Default.Visibility,
                                        contentDescription = "View",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }

                                IconButton(onClick = { onDeleteStory(story.id) }) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteOutline,
                                        contentDescription = "Delete",
                                        tint = QessaRoseTertiary
                                    )
                                }
                            }
                        }
                    }
                }
            }
            3 -> {
                // Users Management
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(allUsers, key = { it.id }) { user ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when (user.role) {
                                                "ADMIN" -> QessaGoldSecondary
                                                "WRITER" -> MaterialTheme.colorScheme.primary
                                                "MODERATOR" -> QessaRoseTertiary
                                                else -> MaterialTheme.colorScheme.surfaceVariant
                                            }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = user.displayName.take(1),
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = user.displayName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                        if (user.isBlocked) {
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = QessaRoseTertiary.copy(alpha = 0.2f),
                                                contentColor = QessaRoseTertiary
                                            ) {
                                                Text(
                                                    text = if (isArabic) "محظور" else "Banned",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        text = "@${user.username} • ${user.role}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                OutlinedButton(
                                    onClick = { onToggleUserBan(user) },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = if (user.isBlocked) MaterialTheme.colorScheme.primary else QessaRoseTertiary
                                    )
                                ) {
                                    Text(
                                        text = if (user.isBlocked) (if (isArabic) "فك الحظر" else "Unban") else (if (isArabic) "حظر" else "Ban"),
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
