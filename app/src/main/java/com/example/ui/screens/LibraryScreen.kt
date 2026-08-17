package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ReadingProgressEntity
import com.example.data.model.StoryEntity
import com.example.ui.components.EmptyStateView
import com.example.ui.components.StoryCardHorizontal

@Composable
fun LibraryScreen(
    userLibrary: List<StoryEntity>,
    readingProgressList: List<ReadingProgressEntity>,
    isArabic: Boolean,
    onOpenStory: (Long) -> Unit,
    onReadStory: (Long) -> Unit,
    onExploreStories: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = if (isArabic) {
        listOf("كل القصص", "أقرأها حالياً", "المفضلة", "قراءة لاحقاً")
    } else {
        listOf("All Stories", "Reading Now", "Favorites", "Read Later")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("library_screen")
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Header Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (isArabic) "مكتبتي الشخصية" else "My Library",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "${userLibrary.size} ${if (isArabic) "قصص محفوظة" else "saved stories"}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = onExploreStories) {
                Icon(
                    imageVector = Icons.Default.AddCircleOutline,
                    contentDescription = "Discover Stories",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Scrollable / Tab Row
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = 0.dp,
            containerColor = MaterialTheme.colorScheme.surface,
            divider = {}
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (userLibrary.isEmpty()) {
            EmptyStateView(
                icon = Icons.Outlined.CollectionsBookmark,
                title = if (isArabic) "مكتبتك فارغة حالياً" else "Your library is empty",
                subtitle = if (isArabic) "استكشف آلاف الروايات الشيقة وأضفها لمكتبتك لقراءتها في أي وقت" else "Discover novels and add them to read anytime",
                actionText = if (isArabic) "استكشف القصص الآن" else "Explore Stories",
                onAction = onExploreStories
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(userLibrary) { story ->
                    StoryCardHorizontal(
                        story = story,
                        isArabic = isArabic,
                        onClick = { onOpenStory(story.id) }
                    )
                }
            }
        }
    }
}
