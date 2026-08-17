package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChapterEntity
import com.example.data.model.CommentEntity
import com.example.data.model.StoryEntity
import com.example.ui.components.CommentsSheet
import com.example.ui.components.ReadingSettingsSheet
import com.example.ui.components.TableOfContentsSheet
import com.example.ui.theme.*
import com.example.viewmodel.ReadingPreferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingScreen(
    story: StoryEntity?,
    chapter: ChapterEntity?,
    allChapters: List<ChapterEntity>,
    comments: List<CommentEntity>,
    preferences: ReadingPreferences,
    isLiked: Boolean,
    isArabic: Boolean,
    onPreferencesChange: (ReadingPreferences) -> Unit,
    onNavigateBack: () -> Unit,
    onNextChapter: () -> Unit,
    onPreviousChapter: () -> Unit,
    onSelectChapter: (ChapterEntity) -> Unit,
    onToggleLike: () -> Unit,
    onAddComment: (String) -> Unit
) {
    if (chapter == null || story == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    var showSettingsSheet by remember { mutableStateOf(false) }
    var showTocSheet by remember { mutableStateOf(false) }
    var showCommentsSheet by remember { mutableStateOf(false) }

    val readerColors = when (preferences.themeMode) {
        "LIGHT" -> Pair(ReaderLightBg, ReaderLightText)
        "DARK" -> Pair(ReaderDarkBg, ReaderDarkText)
        "OLED" -> Pair(ReaderOledBg, ReaderOledText)
        else -> Pair(ReaderSepiaBg, ReaderSepiaText) // SEPIA default
    }

    val readerBg = readerColors.first
    val readerText = readerColors.second

    val scrollState = rememberScrollState()

    val currentIndex = allChapters.indexOfFirst { it.id == chapter.id }
    val hasPrevious = currentIndex > 0
    val hasNext = currentIndex in 0 until allChapters.size - 1

    Scaffold(
        containerColor = readerBg,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = readerBg,
                    titleContentColor = readerText
                ),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack, modifier = Modifier.testTag("reader_back_button")) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = readerText
                        )
                    }
                },
                title = {
                    Column {
                        Text(
                            text = story.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = readerText,
                            maxLines = 1
                        )
                        Text(
                            text = "${if (isArabic) "فصل" else "Ch"} ${chapter.chapterNumber}: ${chapter.title}",
                            fontSize = 11.sp,
                            color = readerText.copy(alpha = 0.7f),
                            maxLines = 1
                        )
                    }
                },
                actions = {
                    // Like Chapter
                    IconButton(onClick = onToggleLike) {
                        Icon(
                            imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (isLiked) QessaRoseTertiary else readerText
                        )
                    }

                    // Comments Button with count
                    IconButton(onClick = { showCommentsSheet = true }) {
                        BadgedBox(
                            badge = {
                                if (comments.isNotEmpty()) {
                                    Badge(containerColor = QessaIndigoPrimary, contentColor = Color.White) {
                                        Text(comments.size.toString())
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ModeComment,
                                contentDescription = "Comments",
                                tint = readerText
                            )
                        }
                    }

                    // Table of contents
                    IconButton(onClick = { showTocSheet = true }) {
                        Icon(
                            imageVector = Icons.Default.FormatListNumbered,
                            contentDescription = "Table of Contents",
                            tint = readerText
                        )
                    }

                    // Font & Theme Settings
                    IconButton(onClick = { showSettingsSheet = true }) {
                        Icon(
                            imageVector = Icons.Default.FormatSize,
                            contentDescription = "Reading Settings",
                            tint = readerText
                        )
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                color = readerBg,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onPreviousChapter,
                        enabled = hasPrevious,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipPrevious,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isArabic) "السابق" else "Prev")
                    }

                    Text(
                        text = "${chapter.chapterNumber} / ${allChapters.size}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = readerText.copy(alpha = 0.7f)
                    )

                    Button(
                        onClick = onNextChapter,
                        enabled = hasNext,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(if (isArabic) "التالي" else "Next")
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .testTag("reading_screen_content"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Chapter Title Header
            Text(
                text = chapter.title,
                fontSize = (preferences.fontSizeSp + 6).sp,
                fontWeight = FontWeight.Bold,
                color = readerText,
                lineHeight = (preferences.fontSizeSp + 14).sp,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${chapter.wordCount} ${if (isArabic) "كلمة" else "words"}",
                    fontSize = 12.sp,
                    color = readerText.copy(alpha = 0.6f)
                )
                Text(
                    text = "•",
                    fontSize = 12.sp,
                    color = readerText.copy(alpha = 0.6f)
                )
                Text(
                    text = "${chapter.wordCount / 60 + 1} ${if (isArabic) "دقائق قراءة" else "min read"}",
                    fontSize = 12.sp,
                    color = readerText.copy(alpha = 0.6f)
                )
            }

            Divider(color = readerText.copy(alpha = 0.15f))

            // Body Paragraphs Rendering with Quote block styling
            chapter.content.lines().forEach { line ->
                val trimmed = line.trim()
                if (trimmed.startsWith(">")) {
                    // Blockquote
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = readerText.copy(alpha = 0.08f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .fillMaxHeight()
                                    .background(QessaGoldSecondary, RoundedCornerShape(2.dp))
                            )
                            Text(
                                text = trimmed.removePrefix(">").trim(),
                                fontSize = preferences.fontSizeSp.sp,
                                lineHeight = (preferences.fontSizeSp * preferences.lineSpacing).sp,
                                fontWeight = FontWeight.Medium,
                                color = readerText,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                        }
                    }
                } else if (trimmed.isNotEmpty()) {
                    Text(
                        text = trimmed,
                        fontSize = preferences.fontSizeSp.sp,
                        lineHeight = (preferences.fontSizeSp * preferences.lineSpacing).sp,
                        color = readerText,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // End of Chapter card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = readerText.copy(alpha = 0.06f))
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = if (isArabic) "نهاية الفصل" else "End of Chapter",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = readerText
                    )
                    Text(
                        text = if (isArabic) "هل استمتعت بهذه الأحداث؟ شارك رأيك مع الكاتب والمجتمع" else "Enjoyed this chapter? Share your review with author and community",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = readerText.copy(alpha = 0.7f)
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        FilledTonalButton(
                            onClick = onToggleLike,
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = if (isLiked) QessaRoseTertiary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Icon(
                                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = null,
                                tint = if (isLiked) QessaRoseTertiary else readerText,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isLiked) (if (isArabic) "معجب به" else "Liked") else (if (isArabic) "أعجبني" else "Like"),
                                color = if (isLiked) QessaRoseTertiary else readerText,
                                fontSize = 12.sp
                            )
                        }

                        Button(
                            onClick = { showCommentsSheet = true },
                            colors = ButtonDefaults.buttonColors(containerColor = QessaIndigoPrimary)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChatBubble,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isArabic) "المناقشات" else "Discuss", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }

    // Sheets
    if (showSettingsSheet) {
        ReadingSettingsSheet(
            preferences = preferences,
            isArabic = isArabic,
            onPreferencesChange = onPreferencesChange,
            onDismiss = { showSettingsSheet = false }
        )
    }

    if (showTocSheet) {
        TableOfContentsSheet(
            chapters = allChapters,
            currentChapterId = chapter.id,
            isArabic = isArabic,
            onSelectChapter = onSelectChapter,
            onDismiss = { showTocSheet = false }
        )
    }

    if (showCommentsSheet) {
        CommentsSheet(
            comments = comments,
            isArabic = isArabic,
            onAddComment = onAddComment,
            onDismiss = { showCommentsSheet = false }
        )
    }
}
