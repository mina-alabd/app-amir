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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StoryEntity
import com.example.data.model.UserEntity
import com.example.ui.components.EmptyStateView
import com.example.ui.components.StatCard
import com.example.ui.components.StoryCardHorizontal
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    currentUser: UserEntity?,
    userStories: List<StoryEntity>,
    isArabic: Boolean,
    isDarkMode: Boolean,
    onOpenStory: (Long) -> Unit,
    onToggleTheme: () -> Unit,
    onToggleLanguage: () -> Unit,
    onOpenUserSwitch: () -> Unit,
    onOpenAdmin: () -> Unit
) {
    if (currentUser == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("profile_screen"),
        contentPadding = PaddingValues(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            // Profile Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(QessaIndigoPrimary, QessaGoldSecondary)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentUser.displayName.take(1),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp
                        )
                    }

                    Text(
                        text = currentUser.displayName,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )

                    Text(
                        text = "@${currentUser.username} • ${currentUser.email}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Role Badge
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = when (currentUser.role) {
                            "ADMIN" -> QessaGoldSecondary.copy(alpha = 0.2f)
                            "MODERATOR" -> QessaRoseTertiary.copy(alpha = 0.2f)
                            "WRITER" -> MaterialTheme.colorScheme.primaryContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        },
                        contentColor = when (currentUser.role) {
                            "ADMIN" -> QessaGoldSecondary
                            "MODERATOR" -> QessaRoseTertiary
                            "WRITER" -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    ) {
                        Text(
                            text = when (currentUser.role) {
                                "ADMIN" -> if (isArabic) "مسؤول النظام (Admin)" else "System Admin"
                                "MODERATOR" -> if (isArabic) "مشرف المحتوى (Moderator)" else "Content Moderator"
                                "WRITER" -> if (isArabic) "كاتب معتمد (Writer)" else "Verified Writer"
                                else -> if (isArabic) "قارئ (Reader)" else "Reader"
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    if (currentUser.bio.isNotBlank()) {
                        Text(
                            text = currentUser.bio,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }

                    Divider(modifier = Modifier.padding(vertical = 4.dp))

                    // Stats Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${currentUser.followersCount}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = if (isArabic) "متابع" else "Followers",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${currentUser.followingCount}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = if (isArabic) "يتابع" else "Following",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${userStories.size}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = if (isArabic) "قصص" else "Stories",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Fast Actions & Settings
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = if (isArabic) "إعدادات الحساب والتطبيق" else "Account & App Settings",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )

                    // Switch Account demo
                    ListItem(
                        headlineContent = { Text(if (isArabic) "تبديل الحساب التجريبي" else "Switch Demo Account") },
                        supportingContent = { Text(if (isArabic) "جرّب أدوار الكاتب، القارئ، المشرف والمسؤول" else "Test Reader, Writer, Mod, and Admin profiles", fontSize = 11.sp) },
                        leadingContent = { Icon(Icons.Default.PeopleAlt, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
                        modifier = Modifier.clickable { onOpenUserSwitch() }
                    )

                    // Dark mode
                    ListItem(
                        headlineContent = { Text(if (isArabic) "المظهر الداكن" else "Dark Mode") },
                        leadingContent = { Icon(Icons.Default.DarkMode, contentDescription = null, tint = QessaGoldSecondary) },
                        trailingContent = {
                            Switch(checked = isDarkMode, onCheckedChange = { onToggleTheme() })
                        }
                    )

                    // Language RTL
                    ListItem(
                        headlineContent = { Text(if (isArabic) "لغة الواجهة (العربية / English)" else "Interface Language") },
                        leadingContent = { Icon(Icons.Default.Language, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        trailingContent = {
                            Button(
                                onClick = onToggleLanguage,
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(if (isArabic) "English" else "العربية", fontSize = 11.sp)
                            }
                        }
                    )

                    // Admin Dashboard if Admin/Mod
                    if (currentUser.role == "ADMIN" || currentUser.role == "MODERATOR") {
                        ListItem(
                            headlineContent = { Text(if (isArabic) "لوحة إدارة المنصة (Admin Dashboard)" else "Admin Dashboard") },
                            supportingContent = { Text(if (isArabic) "إدارة المستخدمين، القصص، البلاغات والتصنيفات" else "Manage users, stories, reports & genres", fontSize = 11.sp) },
                            leadingContent = { Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = QessaGoldSecondary) },
                            trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
                            modifier = Modifier.clickable { onOpenAdmin() }
                        )
                    }
                }
            }
        }

        // Written Stories Tab
        item {
            Text(
                text = if (isArabic) "القصص المنشورة بواسطة ${currentUser.displayName}" else "Published Stories by ${currentUser.displayName}",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        if (userStories.isEmpty()) {
            item {
                EmptyStateView(
                    icon = Icons.Outlined.AutoStories,
                    title = if (isArabic) "لا توجد قصص بعد" else "No published stories",
                    subtitle = if (isArabic) "انتقل إلى استوديو الكاتب وابدأ بنشر روايتك الأولى" else "Head over to the writer studio and publish your first story"
                )
            }
        } else {
            items(userStories) { story ->
                StoryCardHorizontal(
                    story = story,
                    isArabic = isArabic,
                    onClick = { onOpenStory(story.id) }
                )
            }
        }
    }
}
