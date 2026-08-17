package com.example.ui.screens

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
import com.example.data.model.StoryEntity
import com.example.ui.components.CategoryChip
import com.example.ui.components.EmptyStateView
import com.example.ui.components.StoryCardHorizontal
import com.example.viewmodel.SearchFilters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    searchResults: List<StoryEntity>,
    categories: List<CategoryEntity>,
    currentFilters: SearchFilters,
    isArabic: Boolean,
    onUpdateFilters: (SearchFilters) -> Unit,
    onOpenStory: (Long) -> Unit
) {
    var showFilterSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("discover_screen")
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Search Bar
        OutlinedTextField(
            value = currentFilters.query,
            onValueChange = { onUpdateFilters(currentFilters.copy(query = it)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("search_input_field"),
            placeholder = { Text(if (isArabic) "ابحث عن قصة، كاتب، أو وسم..." else "Search stories, authors, tags...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (currentFilters.query.isNotEmpty()) {
                        IconButton(onClick = { onUpdateFilters(currentFilters.copy(query = "")) }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                    IconButton(
                        onClick = { showFilterSheet = true },
                        modifier = Modifier.testTag("open_filters_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Filters",
                            tint = if (currentFilters.status != "ALL" || currentFilters.language != "ALL" || currentFilters.categoryId != null) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                }
            },
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Category Horizontal Strip
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            item {
                FilterChip(
                    selected = currentFilters.categoryId == null || currentFilters.categoryId == "ALL",
                    onClick = { onUpdateFilters(currentFilters.copy(categoryId = "ALL")) },
                    label = { Text(if (isArabic) "الكل" else "All") }
                )
            }
            items(categories) { cat ->
                CategoryChip(
                    category = cat,
                    isSelected = currentFilters.categoryId == cat.id,
                    isArabic = isArabic,
                    onClick = {
                        val newCat = if (currentFilters.categoryId == cat.id) "ALL" else cat.id
                        onUpdateFilters(currentFilters.copy(categoryId = newCat))
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Results Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${if (isArabic) "النتائج" else "Results"} (${searchResults.size})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            // Sort Pill
            AssistChip(
                onClick = {
                    val nextSort = when (currentFilters.sortBy) {
                        "POPULAR" -> "RATING"
                        "RATING" -> "LATEST"
                        "LATEST" -> "LIKES"
                        else -> "POPULAR"
                    }
                    onUpdateFilters(currentFilters.copy(sortBy = nextSort))
                },
                label = {
                    Text(
                        text = when (currentFilters.sortBy) {
                            "POPULAR" -> if (isArabic) "الأكثر قراءة" else "Most Popular"
                            "RATING" -> if (isArabic) "الأعلى تقييماً" else "Top Rated"
                            "LATEST" -> if (isArabic) "الأحدث" else "Latest"
                            else -> if (isArabic) "الأكثر إعجاباً" else "Most Liked"
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Sort,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Results List
        if (searchResults.isEmpty()) {
            EmptyStateView(
                icon = Icons.Outlined.SearchOff,
                title = if (isArabic) "لم يتم العثور على نتائج" else "No stories found",
                subtitle = if (isArabic) "جرب تغيير مصطلحات البحث أو إزالة بعض الفلاتر" else "Try different keywords or clear filters",
                actionText = if (isArabic) "إعادة ضبط الفلاتر" else "Reset Filters",
                onAction = { onUpdateFilters(SearchFilters()) }
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(searchResults) { story ->
                    StoryCardHorizontal(
                        story = story,
                        isArabic = isArabic,
                        onClick = { onOpenStory(story.id) }
                    )
                }
            }
        }
    }

    // Filter Bottom Sheet
    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = if (isArabic) "تصفية متقدمة" else "Advanced Filter",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                // Status Filter
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (isArabic) "حالة القصة" else "Story Status",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(
                            Pair("ALL", if (isArabic) "الكل" else "All"),
                            Pair("ONGOING", if (isArabic) "مستمرة" else "Ongoing"),
                            Pair("COMPLETED", if (isArabic) "مكتملة" else "Completed")
                        ).forEach { (status, label) ->
                            FilterChip(
                                selected = currentFilters.status == status,
                                onClick = { onUpdateFilters(currentFilters.copy(status = status)) },
                                label = { Text(label) }
                            )
                        }
                    }
                }

                // Language Filter
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (isArabic) "اللغة" else "Language",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(
                            Pair("ALL", if (isArabic) "جميع اللغات" else "All Languages"),
                            Pair("ar", "العربية (Arabic)"),
                            Pair("en", "English")
                        ).forEach { (lang, label) ->
                            FilterChip(
                                selected = currentFilters.language == lang,
                                onClick = { onUpdateFilters(currentFilters.copy(language = lang)) },
                                label = { Text(label) }
                            )
                        }
                    }
                }

                Button(
                    onClick = { showFilterSheet = false },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(if (isArabic) "تطبيق الفلاتر" else "Apply Filters")
                }
            }
        }
    }
}
