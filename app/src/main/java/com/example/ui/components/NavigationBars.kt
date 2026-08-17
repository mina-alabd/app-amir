package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserEntity
import com.example.ui.theme.*
import com.example.viewmodel.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QessaTopBar(
    currentScreen: Screen,
    currentUser: UserEntity?,
    unreadNotifications: Int,
    isArabic: Boolean,
    isDarkMode: Boolean,
    onNavigateBack: () -> Unit,
    onOpenNotifications: () -> Unit,
    onOpenAdmin: () -> Unit,
    onToggleTheme: () -> Unit,
    onToggleLanguage: () -> Unit,
    onOpenUserSwitch: () -> Unit
) {
    val showBack = currentScreen != Screen.Home && 
                   currentScreen != Screen.Discover && 
                   currentScreen != Screen.Library && 
                   currentScreen != Screen.WriteStudio && 
                   currentScreen != Screen.Profile

    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("qessa_top_bar"),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (showBack) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("back_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = if (isArabic) "رجوع" else "Back"
                        )
                    }
                }

                // App Brand Logo & Title
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(QessaIndigoPrimary, QessaGoldSecondary)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoStories,
                        contentDescription = "Logo",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (isArabic) "قصّة" else "Qessa",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = QessaGoldSecondary.copy(alpha = 0.2f),
                            contentColor = QessaGoldSecondary
                        ) {
                            Text(
                                text = "PRO",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        },
        actions = {
            // Theme toggle
            IconButton(
                onClick = onToggleTheme,
                modifier = Modifier.testTag("toggle_theme_button")
            ) {
                Icon(
                    imageVector = if (isDarkMode) Icons.Outlined.LightMode else Icons.Outlined.DarkMode,
                    contentDescription = "Toggle Dark/Light",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Language toggle
            IconButton(
                onClick = onToggleLanguage,
                modifier = Modifier.testTag("toggle_lang_button")
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.padding(2.dp)
                ) {
                    Text(
                        text = if (isArabic) "EN" else "عربي",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                    )
                }
            }

            // Admin Dashboard button if Admin/Mod
            if (currentUser?.role == "ADMIN" || currentUser?.role == "MODERATOR") {
                IconButton(
                    onClick = onOpenAdmin,
                    modifier = Modifier.testTag("admin_dashboard_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = "Admin Dashboard",
                        tint = QessaGoldSecondary
                    )
                }
            }

            // Notifications with badge
            IconButton(
                onClick = onOpenNotifications,
                modifier = Modifier.testTag("notifications_button")
            ) {
                BadgedBox(
                    badge = {
                        if (unreadNotifications > 0) {
                            Badge(
                                containerColor = QessaRoseTertiary,
                                contentColor = Color.White
                            ) {
                                Text(unreadNotifications.toString())
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications"
                    )
                }
            }

            // User Avatar Switcher Button
            IconButton(
                onClick = onOpenUserSwitch,
                modifier = Modifier.testTag("switch_user_avatar_button")
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            when (currentUser?.role) {
                                "ADMIN" -> QessaGoldSecondary
                                "WRITER" -> QessaIndigoPrimary
                                "MODERATOR" -> QessaRoseTertiary
                                else -> MaterialTheme.colorScheme.primaryContainer
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentUser?.displayName?.take(1) ?: "U",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }
    )
}

@Composable
fun QessaBottomBar(
    currentScreen: Screen,
    isArabic: Boolean,
    onNavigate: (Screen) -> Unit
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("qessa_bottom_bar"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            Triple(Screen.Home, if (isArabic) "الرئيسية" else "Home", Pair(Icons.Filled.Home, Icons.Outlined.Home)),
            Triple(Screen.Discover, if (isArabic) "استكشف" else "Discover", Pair(Icons.Filled.Explore, Icons.Outlined.Explore)),
            Triple(Screen.Library, if (isArabic) "مكتبتي" else "Library", Pair(Icons.Filled.Bookmarks, Icons.Outlined.Bookmarks)),
            Triple(Screen.WriteStudio, if (isArabic) "اكتب" else "Write", Pair(Icons.Filled.EditNote, Icons.Outlined.EditNote)),
            Triple(Screen.Profile, if (isArabic) "حسابي" else "Profile", Pair(Icons.Filled.Person, Icons.Outlined.Person))
        )

        items.forEach { (screen, label, icons) ->
            val isSelected = currentScreen == screen
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(screen) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) icons.first else icons.second,
                        contentDescription = label
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 12.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag("nav_item_${screen.route}")
            )
        }
    }
}
