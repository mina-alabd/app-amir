package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.data.model.CategoryEntity
import com.example.data.model.ReadingProgressEntity
import com.example.data.model.StoryEntity
import com.example.data.model.UserEntity
import com.example.ui.components.*
import com.example.ui.theme.QessaGoldSecondary
import com.example.ui.theme.QessaIndigoPrimary
import com.example.viewmodel.Screen

@Composable
fun HomeScreen(
    featuredStories: List<StoryEntity>,
    trendingStories: List<StoryEntity>,
    topRatedStories: List<StoryEntity>,
    newArrivals: List<StoryEntity>,
    categories: List<CategoryEntity>,
    topAuthors: List<UserEntity>,
    readingProgressList: List<ReadingProgressEntity>,
    publishedStories: List<StoryEntity>,
    isArabic: Boolean,
    onOpenStory: (Long) -> Unit,
    onReadStory: (Long) -> Unit,
    onSelectCategory: (String) -> Unit,
    onCreateStory: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onCreateStory,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp),
                icon = { Icon(Icons.Default.Edit, contentDescription = null) },
                text = { Text(if (isArabic) "اكتب قصة" else "Write Story", fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("home_fab_write_story")
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("home_screen_lazy_column"),
            contentPadding = PaddingValues(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. Hero Featured Story
            val heroStory = featuredStories.firstOrNull() ?: publishedStories.firstOrNull()
            if (heroStory != null) {
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        FeaturedStoryHero(
                            story = heroStory,
                            isArabic = isArabic,
                            onClick = { onOpenStory(heroStory.id) },
                            onReadClick = { onReadStory(heroStory.id) }
                        )
                    }
                }
            }

            // 2. Continue Reading (if any progress exists)
            val latestProgress = readingProgressList.firstOrNull()
            val progressStory = publishedStories.find { it.id == latestProgress?.storyId }
            if (progressStory != null && latestProgress != null) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (isArabic) "تابع القراءة" else "Continue Reading",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onReadStory(progressStory.id) }
                                .testTag("continue_reading_card"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
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
                                        .size(width = 50.dp, height = 70.dp)
                                ) {
                                    StoryCoverImage(
                                        coverResName = progressStory.coverResName,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = progressStory.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "${if (isArabic) "الفصل" else "Chapter"} ${latestProgress.lastChapterNumber}",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    LinearProgressIndicator(
                                        progress = { latestProgress.scrollProgress },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(6.dp),
                                        color = MaterialTheme.colorScheme.primary,
                                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                    )
                                }

                                Button(
                                    onClick = { onReadStory(progressStory.id) },
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = if (isArabic) "استئناف" else "Resume",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 3. Category Pills
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = if (isArabic) "التصنيفات الأدبية" else "Literary Genres",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(categories) { cat ->
                            CategoryChip(
                                category = cat,
                                isSelected = false,
                                isArabic = isArabic,
                                onClick = { onSelectCategory(cat.id) }
                            )
                        }
                    }
                }
            }

            // 4. Trending Stories Carousel
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isArabic) "القصص الأكثر رواجاً 🔥" else "Trending Stories 🔥",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(trendingStories) { story ->
                            StoryCardVertical(
                                story = story,
                                onClick = { onOpenStory(story.id) }
                            )
                        }
                    }
                }
            }

            // 5. Top Rated Section
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = if (isArabic) "الأعلى تقييماً ⭐" else "Top Rated ⭐",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    topRatedStories.take(3).forEach { story ->
                        StoryCardHorizontal(
                            story = story,
                            isArabic = isArabic,
                            onClick = { onOpenStory(story.id) }
                        )
                    }
                }
            }

            // 6. Popular Authors
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = if (isArabic) "كتّاب ملهمون ✍️" else "Featured Authors ✍️",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(topAuthors) { author ->
                            AuthorCard(
                                author = author,
                                isArabic = isArabic,
                                onClick = {}
                            )
                        }
                    }
                }
            }

            // 7. New Arrivals Section
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = if (isArabic) "أحدث الإصدارات 🚀" else "New Arrivals 🚀",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    newArrivals.take(3).forEach { story ->
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
}
