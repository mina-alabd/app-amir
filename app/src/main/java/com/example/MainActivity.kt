package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.LayoutDirection
import com.example.ui.components.QessaBottomBar
import com.example.ui.components.QessaTopBar
import com.example.ui.components.UserSwitchDialog
import com.example.ui.screens.*
import com.example.ui.theme.QessaTheme
import com.example.viewmodel.QessaViewModel
import com.example.viewmodel.Screen
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    private val viewModel: QessaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkMode by viewModel.isDarkMode.collectAsState()
            val isArabic by viewModel.isArabic.collectAsState()

            QessaTheme(darkTheme = isDarkMode) {
                CompositionLocalProvider(
                    LocalLayoutDirection provides if (isArabic) LayoutDirection.Rtl else LayoutDirection.Ltr
                ) {
                    QessaMainApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun QessaMainApp(viewModel: QessaViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val allUsers by viewModel.allUsers.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val isArabic by viewModel.isArabic.collectAsState()
    val unreadNotifications by viewModel.unreadNotificationsCount.collectAsState()

    var showUserSwitchDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Collect snackbar messages
    LaunchedEffect(Unit) {
        viewModel.snackBarMessage.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    // Handle system back button
    BackHandler(enabled = currentScreen != Screen.Home) {
        if (!viewModel.navigateBack()) {
            // Already at root
        }
    }

    // Data streams
    val featuredStories by viewModel.featuredStories.collectAsState()
    val trendingStories by viewModel.trendingStories.collectAsState()
    val topRatedStories by viewModel.topRatedStories.collectAsState()
    val newArrivals by viewModel.newArrivals.collectAsState()
    val publishedStories by viewModel.publishedStories.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val topAuthors by viewModel.topAuthors.collectAsState()
    val readingProgressList by viewModel.readingProgressList.collectAsState()

    // Screen specific states
    val searchResults by viewModel.searchResults.collectAsState()
    val searchFilters by viewModel.searchFilters.collectAsState()
    val userLibrary by viewModel.userLibrary.collectAsState()
    val authorStories by viewModel.authorStories.collectAsState()
    val userNotifications by viewModel.userNotifications.collectAsState()
    val selectedStory by viewModel.selectedStory.collectAsState()
    val selectedStoryChapters by viewModel.selectedStoryChapters.collectAsState()
    val allSelectedStoryChapters by viewModel.allSelectedStoryChapters.collectAsState()
    val selectedStoryComments by viewModel.selectedStoryComments.collectAsState()
    val selectedStoryRatings by viewModel.selectedStoryRatings.collectAsState()
    val isSelectedStoryInLibrary by viewModel.isSelectedStoryInLibrary.collectAsState()
    val isSelectedStoryLiked by viewModel.isSelectedStoryLiked.collectAsState()
    val isAuthorFollowed by viewModel.isAuthorFollowed.collectAsState()
    val currentChapter by viewModel.currentChapter.collectAsState()
    val readingPreferences by viewModel.readingPreferences.collectAsState()

    // Admin states
    val allReports by viewModel.allReports.collectAsState()
    val totalStoriesCount by viewModel.totalStoriesCount.collectAsState()
    val totalChaptersCount by viewModel.totalChaptersCount.collectAsState()
    val totalCommentsCount by viewModel.totalCommentsCount.collectAsState()
    val totalReads by viewModel.totalReads.collectAsState()

    val isTopLevelScreen = currentScreen == Screen.Home ||
            currentScreen == Screen.Discover ||
            currentScreen == Screen.Library ||
            currentScreen == Screen.WriteStudio ||
            currentScreen == Screen.Profile

    val isReadingScreen = currentScreen == Screen.Reading

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("qessa_main_scaffold"),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (!isReadingScreen) {
                QessaTopBar(
                    currentScreen = currentScreen,
                    currentUser = currentUser,
                    unreadNotifications = unreadNotifications,
                    isArabic = isArabic,
                    isDarkMode = isDarkMode,
                    onNavigateBack = { viewModel.navigateBack() },
                    onOpenNotifications = { viewModel.navigateTo(Screen.Notifications) },
                    onOpenAdmin = { viewModel.navigateTo(Screen.AdminDashboard) },
                    onToggleTheme = { viewModel.toggleDarkMode() },
                    onToggleLanguage = { viewModel.toggleLanguage() },
                    onOpenUserSwitch = { showUserSwitchDialog = true }
                )
            }
        },
        bottomBar = {
            if (isTopLevelScreen) {
                QessaBottomBar(
                    currentScreen = currentScreen,
                    isArabic = isArabic,
                    onNavigate = { screen -> viewModel.navigateTo(screen) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                Screen.Home -> HomeScreen(
                    featuredStories = featuredStories,
                    trendingStories = trendingStories,
                    topRatedStories = topRatedStories,
                    newArrivals = newArrivals,
                    categories = categories,
                    topAuthors = topAuthors,
                    readingProgressList = readingProgressList,
                    publishedStories = publishedStories,
                    isArabic = isArabic,
                    onOpenStory = { id -> viewModel.openStoryDetail(id) },
                    onReadStory = { id -> viewModel.openReading(id) },
                    onSelectCategory = { catId ->
                        viewModel.searchFilters.value = searchFilters.copy(categoryId = catId)
                        viewModel.navigateTo(Screen.Discover)
                    },
                    onCreateStory = { viewModel.navigateTo(Screen.WriteStudio) }
                )

                Screen.Discover -> DiscoverScreen(
                    searchResults = searchResults,
                    categories = categories,
                    currentFilters = searchFilters,
                    isArabic = isArabic,
                    onUpdateFilters = { newFilters -> viewModel.searchFilters.value = newFilters },
                    onOpenStory = { id -> viewModel.openStoryDetail(id) }
                )

                Screen.Library -> LibraryScreen(
                    userLibrary = userLibrary,
                    readingProgressList = readingProgressList,
                    isArabic = isArabic,
                    onOpenStory = { id -> viewModel.openStoryDetail(id) },
                    onReadStory = { id -> viewModel.openReading(id) },
                    onExploreStories = { viewModel.navigateTo(Screen.Discover) }
                )

                Screen.WriteStudio, Screen.CreateStory, Screen.ChapterEditor -> WriteStudioScreen(
                    currentUser = currentUser,
                    authorStories = authorStories,
                    categories = categories,
                    isArabic = isArabic,
                    onOpenStory = { id -> viewModel.openStoryDetail(id) },
                    onCreateStory = { title, desc, cat, tags, lang, age, cover, chTitle, chContent ->
                        viewModel.createStory(title, desc, cat, tags, lang, age, cover, chTitle, chContent)
                    },
                    onAddNewChapter = { storyId, title, content, isDraft ->
                        viewModel.addNewChapter(storyId, title, content, isDraft)
                    }
                )

                Screen.Profile -> ProfileScreen(
                    currentUser = currentUser,
                    userStories = authorStories,
                    isArabic = isArabic,
                    isDarkMode = isDarkMode,
                    onOpenStory = { id -> viewModel.openStoryDetail(id) },
                    onToggleTheme = { viewModel.toggleDarkMode() },
                    onToggleLanguage = { viewModel.toggleLanguage() },
                    onOpenUserSwitch = { showUserSwitchDialog = true },
                    onOpenAdmin = { viewModel.navigateTo(Screen.AdminDashboard) }
                )

                Screen.Notifications -> NotificationsScreen(
                    notifications = userNotifications,
                    unreadCount = unreadNotifications,
                    isArabic = isArabic,
                    onMarkRead = { id -> viewModel.markNotificationRead(id) },
                    onMarkAllRead = { viewModel.markAllNotificationsRead() },
                    onOpenStory = { storyId -> viewModel.openStoryDetail(storyId) }
                )

                Screen.StoryDetail -> StoryDetailScreen(
                    story = selectedStory,
                    chapters = selectedStoryChapters,
                    comments = selectedStoryComments,
                    ratings = selectedStoryRatings,
                    isInLibrary = isSelectedStoryInLibrary,
                    isLiked = isSelectedStoryLiked,
                    isAuthorFollowed = isAuthorFollowed,
                    isArabic = isArabic,
                    onStartReading = { storyId, chapterId -> viewModel.openReading(storyId, chapterId) },
                    onToggleLibrary = { viewModel.toggleLibrary() },
                    onToggleLike = { viewModel.toggleStoryLike() },
                    onToggleFollow = { viewModel.toggleFollowAuthor() },
                    onSubmitRating = { stars, review -> viewModel.submitStoryRating(stars, review) },
                    onAddComment = { text -> viewModel.addComment(text) },
                    onSubmitReport = { targetType, targetId, targetTitle, reason ->
                        viewModel.submitReport(targetType, targetId, targetTitle, reason)
                    }
                )

                Screen.Reading -> ReadingScreen(
                    story = selectedStory,
                    chapter = currentChapter,
                    allChapters = selectedStoryChapters,
                    comments = selectedStoryComments,
                    preferences = readingPreferences,
                    isLiked = isSelectedStoryLiked,
                    isArabic = isArabic,
                    onPreferencesChange = { prefs -> viewModel.readingPreferences.value = prefs },
                    onNavigateBack = { viewModel.navigateBack() },
                    onNextChapter = { viewModel.nextChapter() },
                    onPreviousChapter = { viewModel.previousChapter() },
                    onSelectChapter = { chapter -> viewModel.openReading(chapter.storyId, chapter.id) },
                    onToggleLike = { viewModel.toggleStoryLike() },
                    onAddComment = { text -> viewModel.addComment(text, currentChapter?.id) }
                )

                Screen.AdminDashboard -> AdminDashboardScreen(
                    currentUser = currentUser,
                    allUsers = allUsers,
                    allStories = publishedStories,
                    allReports = allReports,
                    totalStoriesCount = totalStoriesCount,
                    totalChaptersCount = totalChaptersCount,
                    totalCommentsCount = totalCommentsCount,
                    totalReads = totalReads,
                    isArabic = isArabic,
                    onToggleUserBan = { user -> viewModel.toggleUserBan(user) },
                    onChangeUserRole = { userId, newRole -> viewModel.changeUserRole(userId, newRole) },
                    onToggleStoryFeatured = { story -> viewModel.toggleStoryFeatured(story) },
                    onDeleteStory = { storyId -> viewModel.deleteStory(storyId) },
                    onResolveReport = { reportId, action -> viewModel.resolveReport(reportId, action) },
                    onOpenStory = { id -> viewModel.openStoryDetail(id) }
                )
            }
        }
    }

    if (showUserSwitchDialog) {
        UserSwitchDialog(
            currentUser = currentUser,
            users = allUsers,
            isArabic = isArabic,
            onSelectUser = { id -> viewModel.switchUser(id) },
            onDismiss = { showUserSwitchDialog = false }
        )
    }
}
