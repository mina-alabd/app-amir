package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.CategoryEntity
import com.example.data.model.StoryEntity
import com.example.data.model.UserEntity
import com.example.ui.components.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteStudioScreen(
    currentUser: UserEntity?,
    authorStories: List<StoryEntity>,
    categories: List<CategoryEntity>,
    isArabic: Boolean,
    onOpenStory: (Long) -> Unit,
    onCreateStory: (String, String, String, String, String, String, String, String, String) -> Unit,
    onAddNewChapter: (Long, String, String, Boolean) -> Unit
) {
    var showCreateStoryDialog by remember { mutableStateOf(false) }
    var showAddChapterDialog by remember { mutableStateOf(false) }
    var selectedStoryForChapter by remember { mutableStateOf<StoryEntity?>(null) }

    // Dialog state for new story
    var newTitle by remember { mutableStateOf("") }
    var newDesc by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(categories.firstOrNull()?.id ?: "fantasy") }
    var newTags by remember { mutableStateOf("") }
    var selectedLanguage by remember { mutableStateOf("ar") }
    var selectedAgeRating by remember { mutableStateOf("ALL") }
    var selectedCover by remember { mutableStateOf("cover_desert_stars") }
    var firstChapterTitle by remember { mutableStateOf("الفصل الأول: البداية") }
    var firstChapterContent by remember { mutableStateOf("") }

    // Dialog state for new chapter
    var newChapterTitle by remember { mutableStateOf("") }
    var newChapterContent by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showCreateStoryDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp),
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text(if (isArabic) "رواية جديدة" else "New Story", fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("fab_create_new_story")
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .testTag("write_studio_screen"),
            contentPadding = PaddingValues(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                // Title Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isArabic) "استوديو الكاتب" else "Writer Studio",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = if (isArabic) "مرحباً ${currentUser?.displayName ?: ""}، أطلق العنان لإبداعك" else "Welcome back, craft your next masterpiece",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Stats Cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = if (isArabic) "إجمالي القراءات" else "Total Reads",
                        value = "${currentUser?.readsReceived ?: 28400}",
                        icon = Icons.Default.Visibility,
                        iconColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = if (isArabic) "المتابعون" else "Followers",
                        value = "${currentUser?.followersCount ?: 1420}",
                        icon = Icons.Default.People,
                        iconColor = QessaGoldSecondary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Stories Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isArabic) "قصصي المنشورة والمسودات (${authorStories.size})" else "My Stories & Drafts (${authorStories.size})",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            if (authorStories.isEmpty()) {
                item {
                    EmptyStateView(
                        icon = Icons.Outlined.EditNote,
                        title = if (isArabic) "لم تنشر أي رواية بعد" else "No stories written yet",
                        subtitle = if (isArabic) "ابدأ كتابة أول فصول روايتك وشاركها مع ملايين القراء حول العالم" else "Start writing your first chapters and share with readers",
                        actionText = if (isArabic) "اكتب قصتك الأولى" else "Write First Story",
                        onAction = { showCreateStoryDialog = true }
                    )
                }
            } else {
                items(authorStories) { story ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("author_story_card_${story.id}"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(width = 60.dp, height = 85.dp)
                                ) {
                                    StoryCoverImage(
                                        coverResName = story.coverResName,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }

                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = if (story.isPublished) QessaGoldSecondary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                                            contentColor = if (story.isPublished) QessaGoldSecondary else MaterialTheme.colorScheme.onSurfaceVariant
                                        ) {
                                            Text(
                                                text = if (story.isPublished) (if (isArabic) "منشورة" else "Published") else (if (isArabic) "مسودة" else "Draft"),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }

                                        Text(
                                            text = "${story.chaptersCount} ${if (isArabic) "فصل" else "chapters"}",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Text(
                                        text = story.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Text(
                                        text = "${story.readsCount} ${if (isArabic) "قراءة" else "reads"} • ${story.likesCount} ${if (isArabic) "إعجاب" else "likes"}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedButton(
                                    onClick = { onOpenStory(story.id) },
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Text(if (isArabic) "معاينة" else "Preview", fontSize = 12.sp)
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Button(
                                    onClick = {
                                        selectedStoryForChapter = story
                                        showAddChapterDialog = true
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(if (isArabic) "إضافة فصل" else "Add Chapter", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Create Story Full Dialog
    if (showCreateStoryDialog) {
        Dialog(onDismissRequest = { showCreateStoryDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .testTag("create_story_dialog"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isArabic) "إنشاء رواية جديدة" else "Create New Story",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        IconButton(onClick = { showCreateStoryDialog = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text(if (isArabic) "عنوان الرواية" else "Story Title") },
                        placeholder = { Text(if (isArabic) "مثال: واحة الأسرار الضائعة" else "e.g. The Lost Secrets") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = newDesc,
                        onValueChange = { newDesc = it },
                        label = { Text(if (isArabic) "وصف الرواية / النبذة" else "Description") },
                        placeholder = { Text(if (isArabic) "اكتب ملخصاً مشوقاً يجذب القراء..." else "Write a catchy synopsis...") },
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Category selection
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = if (isArabic) "النوع الأدبي" else "Genre",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            categories.take(3).forEach { cat ->
                                FilterChip(
                                    selected = selectedCategory == cat.id,
                                    onClick = { selectedCategory = cat.id },
                                    label = { Text(if (isArabic) cat.nameAr else cat.nameEn, fontSize = 11.sp) }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = newTags,
                        onValueChange = { newTags = it },
                        label = { Text(if (isArabic) "الوسوم (مفصولة بفواصل)" else "Tags (comma-separated)") },
                        placeholder = { Text("فانتازيا, مغامرة, سحر, تشويق") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Divider()

                    Text(
                        text = if (isArabic) "الفصل الأول" else "First Chapter",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    OutlinedTextField(
                        value = firstChapterTitle,
                        onValueChange = { firstChapterTitle = it },
                        label = { Text(if (isArabic) "عنوان الفصل الأول" else "First Chapter Title") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = firstChapterContent,
                        onValueChange = { firstChapterContent = it },
                        label = { Text(if (isArabic) "محتوى الفصل الأول" else "First Chapter Content") },
                        placeholder = { Text(if (isArabic) "اكتب بداية القصة هنا..." else "Start writing here...") },
                        modifier = Modifier.fillMaxWidth().height(150.dp),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Button(
                        onClick = {
                            if (newTitle.isNotBlank()) {
                                onCreateStory(
                                    newTitle,
                                    newDesc,
                                    selectedCategory,
                                    newTags,
                                    selectedLanguage,
                                    selectedAgeRating,
                                    selectedCover,
                                    firstChapterTitle,
                                    firstChapterContent
                                )
                                showCreateStoryDialog = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (isArabic) "نشر الرواية والبدء 🎉" else "Publish Story 🎉", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // Add Chapter Rich Editor Dialog
    if (showAddChapterDialog && selectedStoryForChapter != null) {
        Dialog(onDismissRequest = { showAddChapterDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .testTag("add_chapter_dialog"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (isArabic) "إضافة فصل جديد" else "Add New Chapter",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = selectedStoryForChapter?.title ?: "",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(onClick = { showAddChapterDialog = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    OutlinedTextField(
                        value = newChapterTitle,
                        onValueChange = { newChapterTitle = it },
                        label = { Text(if (isArabic) "عنوان الفصل" else "Chapter Title") },
                        placeholder = { Text(if (isArabic) "مثال: لقاء غير متوقع في الحديقة" else "e.g. Unexpected Encounter") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Rich text formatting helper buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        IconButton(
                            onClick = { newChapterContent += " **نص عريض** " },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.Default.FormatBold, contentDescription = "Bold", modifier = Modifier.size(18.dp))
                        }
                        IconButton(
                            onClick = { newChapterContent += " *نص مائل* " },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.Default.FormatItalic, contentDescription = "Italic", modifier = Modifier.size(18.dp))
                        }
                        IconButton(
                            onClick = { newChapterContent += "\n> «اقتباس مهم هنا»\n" },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.Default.FormatQuote, contentDescription = "Quote", modifier = Modifier.size(18.dp))
                        }
                        IconButton(
                            onClick = { newChapterContent += "\n\n---\n\n" },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.Default.HorizontalRule, contentDescription = "Divider", modifier = Modifier.size(18.dp))
                        }
                    }

                    OutlinedTextField(
                        value = newChapterContent,
                        onValueChange = { newChapterContent = it },
                        label = { Text(if (isArabic) "محتوى الفصل" else "Chapter Content") },
                        placeholder = { Text(if (isArabic) "اكتب تفاصيل الأحداث والحوارات..." else "Write chapter narrative...") },
                        modifier = Modifier.fillMaxWidth().height(220.dp),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Words count and reading time indicator
                    val wordCount = newChapterContent.split("\\s+".toRegex()).filter { it.isNotBlank() }.size
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "$wordCount ${if (isArabic) "كلمة" else "words"}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${wordCount / 60 + 1} ${if (isArabic) "دقائق قراءة متوقعة" else "min estimated read"}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                if (newChapterTitle.isNotBlank()) {
                                    onAddNewChapter(selectedStoryForChapter!!.id, newChapterTitle, newChapterContent, true)
                                    showAddChapterDialog = false
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(if (isArabic) "حفظ كمسودة" else "Save Draft")
                        }

                        Button(
                            onClick = {
                                if (newChapterTitle.isNotBlank()) {
                                    onAddNewChapter(selectedStoryForChapter!!.id, newChapterTitle, newChapterContent, false)
                                    showAddChapterDialog = false
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(if (isArabic) "نشر الفصل" else "Publish", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
