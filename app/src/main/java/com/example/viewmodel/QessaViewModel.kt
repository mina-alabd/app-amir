package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.QessaDatabase
import com.example.data.model.*
import com.example.data.repository.QessaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Discover : Screen("discover")
    object Library : Screen("library")
    object WriteStudio : Screen("write_studio")
    object Profile : Screen("profile")
    object Notifications : Screen("notifications")
    object AdminDashboard : Screen("admin_dashboard")
    object StoryDetail : Screen("story_detail")
    object Reading : Screen("reading")
    object CreateStory : Screen("create_story")
    object ChapterEditor : Screen("chapter_editor")
}

data class ReadingPreferences(
    val fontSizeSp: Float = 18f,
    val fontFamily: String = "DEFAULT", // DEFAULT, SERIF, SANS
    val themeMode: String = "SEPIA", // LIGHT, SEPIA, DARK, OLED
    val lineSpacing: Float = 1.8f,
    val isDistractionFree: Boolean = false
)

data class SearchFilters(
    val query: String = "",
    val categoryId: String? = null,
    val status: String = "ALL", // ALL, ONGOING, COMPLETED
    val language: String = "ALL", // ALL, ar, en
    val sortBy: String = "POPULAR" // POPULAR, RATING, LATEST, LIKES
)

class QessaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: QessaRepository
    
    init {
        val db = QessaDatabase.getDatabase(application, viewModelScope)
        repository = QessaRepository(db)
    }

    // Navigation & App State
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _navigationStack = MutableStateFlow<List<Screen>>(listOf(Screen.Home))

    private val _isDarkMode = MutableStateFlow(true)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _isArabic = MutableStateFlow(true)
    val isArabic: StateFlow<Boolean> = _isArabic.asStateFlow()

    private val _snackBarMessage = MutableSharedFlow<String>()
    val snackBarMessage = _snackBarMessage.asSharedFlow()

    // Auth & Current User
    private val _currentUserId = MutableStateFlow<Long>(1) // Default: Mina Alabd (Admin/Writer)
    val currentUserId: StateFlow<Long> = _currentUserId.asStateFlow()

    val currentUser: StateFlow<UserEntity?> = _currentUserId
        .flatMapLatest { id -> repository.getUserById(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allUsers: StateFlow<List<UserEntity>> = repository.getAllUsers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val topAuthors: StateFlow<List<UserEntity>> = repository.getTopAuthors()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Stories Streams
    val publishedStories: StateFlow<List<StoryEntity>> = repository.getAllPublishedStories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val featuredStories: StateFlow<List<StoryEntity>> = repository.getFeaturedStories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val trendingStories: StateFlow<List<StoryEntity>> = repository.getTrendingStories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val topRatedStories: StateFlow<List<StoryEntity>> = repository.getTopRatedStories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val newArrivals: StateFlow<List<StoryEntity>> = repository.getNewArrivals()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories: StateFlow<List<CategoryEntity>> = repository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Selected Story & Chapter for Detail & Reader
    private val _selectedStoryId = MutableStateFlow<Long?>(1)
    val selectedStoryId: StateFlow<Long?> = _selectedStoryId.asStateFlow()

    val selectedStory: StateFlow<StoryEntity?> = _selectedStoryId
        .flatMapLatest { id -> if (id != null) repository.getStoryById(id) else flowOf(null) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val selectedStoryChapters: StateFlow<List<ChapterEntity>> = _selectedStoryId
        .flatMapLatest { id -> if (id != null) repository.getPublishedChaptersForStory(id) else flowOf(emptyList()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allSelectedStoryChapters: StateFlow<List<ChapterEntity>> = _selectedStoryId
        .flatMapLatest { id -> if (id != null) repository.getChaptersForStory(id) else flowOf(emptyList()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val selectedStoryComments: StateFlow<List<CommentEntity>> = _selectedStoryId
        .flatMapLatest { id -> if (id != null) repository.getStoryComments(id) else flowOf(emptyList()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val selectedStoryRatings: StateFlow<List<RatingEntity>> = _selectedStoryId
        .flatMapLatest { id -> if (id != null) repository.getStoryRatings(id) else flowOf(emptyList()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val isSelectedStoryInLibrary: StateFlow<Boolean> = combine(_currentUserId, _selectedStoryId) { userId, storyId ->
        if (storyId != null) repository.observeLibraryItem(userId, storyId) else flowOf(null)
    }.flatMapLatest { it }
    .map { it != null }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val isSelectedStoryLiked: StateFlow<Boolean> = combine(_currentUserId, _selectedStoryId) { userId, storyId ->
        if (storyId != null) repository.isStoryLiked(userId, storyId) else flowOf(null)
    }.flatMapLatest { it }
    .map { it != null }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val isAuthorFollowed: StateFlow<Boolean> = combine(_currentUserId, selectedStory) { userId, story ->
        if (story != null && story.authorId != userId) repository.isFollowing(userId, story.authorId) else flowOf(null)
    }.flatMapLatest { it }
    .map { it != null }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    // Reader Mode State
    private val _selectedChapterId = MutableStateFlow<Long?>(1)
    val selectedChapterId: StateFlow<Long?> = _selectedChapterId.asStateFlow()

    val currentChapter: StateFlow<ChapterEntity?> = _selectedChapterId
        .flatMapLatest { id -> if (id != null) repository.getChapterById(id) else flowOf(null) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val readingPreferences = MutableStateFlow(ReadingPreferences())

    // Library State
    val userLibrary: StateFlow<List<StoryEntity>> = _currentUserId
        .flatMapLatest { id -> repository.getAllUserLibraryStories(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val readingProgressList: StateFlow<List<ReadingProgressEntity>> = _currentUserId
        .flatMapLatest { id -> repository.getAllReadingProgress(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Author Studio State
    val authorStories: StateFlow<List<StoryEntity>> = _currentUserId
        .flatMapLatest { id -> repository.getStoriesByAuthor(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Notifications State
    val userNotifications: StateFlow<List<NotificationEntity>> = _currentUserId
        .flatMapLatest { id -> repository.getUserNotifications(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unreadNotificationsCount: StateFlow<Int> = _currentUserId
        .flatMapLatest { id -> repository.getUnreadNotificationsCount(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Admin Dashboard State
    val allReports: StateFlow<List<ReportEntity>> = repository.getAllReports()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalStoriesCount: StateFlow<Int> = repository.getStoriesCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalChaptersCount: StateFlow<Int> = repository.getTotalChaptersCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalCommentsCount: StateFlow<Int> = repository.getTotalCommentsCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalReads: StateFlow<Int> = repository.getTotalReads()
        .map { it ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Search Filters & Results
    val searchFilters = MutableStateFlow(SearchFilters())

    val searchResults: StateFlow<List<StoryEntity>> = searchFilters
        .flatMapLatest { filters ->
            repository.searchStories(filters.query).map { list ->
                var result = list
                if (!filters.categoryId.isNullOrBlank() && filters.categoryId != "ALL") {
                    result = result.filter { it.categoryId == filters.categoryId }
                }
                if (filters.status != "ALL") {
                    result = result.filter { it.status == filters.status }
                }
                if (filters.language != "ALL") {
                    result = result.filter { it.language == filters.language }
                }
                when (filters.sortBy) {
                    "POPULAR" -> result.sortedByDescending { it.readsCount }
                    "RATING" -> result.sortedByDescending { it.ratingAvg }
                    "LATEST" -> result.sortedByDescending { it.createdAt }
                    "LIKES" -> result.sortedByDescending { it.likesCount }
                    else -> result
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Navigation Methods
    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            val currentList = _navigationStack.value.toMutableList()
            currentList.add(screen)
            _navigationStack.value = currentList
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        val currentList = _navigationStack.value.toMutableList()
        if (currentList.size > 1) {
            currentList.removeAt(currentList.size - 1)
            _navigationStack.value = currentList
            _currentScreen.value = currentList.last()
            return true
        }
        return false
    }

    fun openStoryDetail(storyId: Long) {
        _selectedStoryId.value = storyId
        navigateTo(Screen.StoryDetail)
    }

    fun openReading(storyId: Long, chapterId: Long? = null) {
        _selectedStoryId.value = storyId
        viewModelScope.launch(Dispatchers.IO) {
            repository.incrementReads(storyId)
            val chapters = repository.getPublishedChaptersForStory(storyId).first()
            val targetChapter = if (chapterId != null) {
                chapters.find { it.id == chapterId } ?: chapters.firstOrNull()
            } else {
                chapters.firstOrNull()
            }

            if (targetChapter != null) {
                _selectedChapterId.value = targetChapter.id
                repository.incrementChapterReads(targetChapter.id)
                repository.saveReadingProgress(
                    userId = _currentUserId.value,
                    storyId = storyId,
                    chapterId = targetChapter.id,
                    chapterNumber = targetChapter.chapterNumber,
                    progress = 0.1f
                )
            }
        }
        navigateTo(Screen.Reading)
    }

    fun nextChapter() {
        val current = currentChapter.value ?: return
        val chapters = selectedStoryChapters.value
        val currentIndex = chapters.indexOfFirst { it.id == current.id }
        if (currentIndex in 0 until chapters.size - 1) {
            val next = chapters[currentIndex + 1]
            _selectedChapterId.value = next.id
            viewModelScope.launch(Dispatchers.IO) {
                repository.incrementChapterReads(next.id)
                _selectedStoryId.value?.let { storyId ->
                    repository.saveReadingProgress(_currentUserId.value, storyId, next.id, next.chapterNumber, 0.1f)
                }
            }
        }
    }

    fun previousChapter() {
        val current = currentChapter.value ?: return
        val chapters = selectedStoryChapters.value
        val currentIndex = chapters.indexOfFirst { it.id == current.id }
        if (currentIndex > 0) {
            val prev = chapters[currentIndex - 1]
            _selectedChapterId.value = prev.id
        }
    }

    // User Operations
    fun switchUser(userId: Long) {
        _currentUserId.value = userId
        emitMessage(if (_isArabic.value) "تم التبديل إلى المستخدم بنجاح" else "Switched user successfully")
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun toggleLanguage() {
        _isArabic.value = !_isArabic.value
    }

    fun toggleStoryLike() {
        val storyId = _selectedStoryId.value ?: return
        val userId = _currentUserId.value
        val isLiked = isSelectedStoryLiked.value
        viewModelScope.launch(Dispatchers.IO) {
            repository.toggleStoryLike(userId, storyId, isLiked)
            emitMessage(if (_isArabic.value) (if (isLiked) "تم إلغاء الإعجاب" else "تم الإعجاب بالقصة ❤️") else (if (isLiked) "Unliked story" else "Liked story ❤️"))
        }
    }

    fun toggleFollowAuthor() {
        val story = selectedStory.value ?: return
        val userId = _currentUserId.value
        if (story.authorId == userId) return
        val isFollowing = isAuthorFollowed.value
        viewModelScope.launch(Dispatchers.IO) {
            repository.toggleFollow(userId, story.authorId, isFollowing)
            emitMessage(if (_isArabic.value) (if (isFollowing) "تم إلغاء المتابعة" else "أنت الآن تتابع ${story.authorName}") else (if (isFollowing) "Unfollowed" else "Following ${story.authorName}"))
        }
    }

    fun toggleLibrary(listType: String = "CURRENT") {
        val storyId = _selectedStoryId.value ?: return
        val userId = _currentUserId.value
        val inLibrary = isSelectedStoryInLibrary.value
        viewModelScope.launch(Dispatchers.IO) {
            if (inLibrary) {
                repository.removeFromLibrary(userId, storyId)
                emitMessage(if (_isArabic.value) "تمت الإزالة من المكتبة" else "Removed from library")
            } else {
                repository.addToLibrary(userId, storyId, listType)
                emitMessage(if (_isArabic.value) "تمت الإضافة إلى المكتبة 📚" else "Added to library 📚")
            }
        }
    }

    fun addComment(text: String, chapterId: Long? = null) {
        if (text.isBlank()) return
        val storyId = _selectedStoryId.value ?: return
        val user = currentUser.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            repository.addComment(
                CommentEntity(
                    storyId = storyId,
                    chapterId = chapterId,
                    userId = user.id,
                    userName = user.displayName,
                    content = text.trim()
                )
            )
            emitMessage(if (_isArabic.value) "تم نشر تعليقك بنجاح" else "Comment published successfully")
        }
    }

    fun submitStoryRating(stars: Int, review: String) {
        val storyId = _selectedStoryId.value ?: return
        val userId = _currentUserId.value
        viewModelScope.launch(Dispatchers.IO) {
            repository.submitRating(
                RatingEntity(
                    userId = userId,
                    storyId = storyId,
                    stars = stars,
                    review = review.trim()
                )
            )
            emitMessage(if (_isArabic.value) "شكراً لتقييمك الرائع!" else "Thank you for your rating!")
        }
    }

    fun markNotificationRead(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.markNotificationRead(id)
        }
    }

    fun markAllNotificationsRead() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.markAllNotificationsRead(_currentUserId.value)
            emitMessage(if (_isArabic.value) "تم تعيين جميع الإشعارات كمقروءة" else "All notifications marked as read")
        }
    }

    // Story & Chapter Creation / Editing
    fun createStory(
        title: String,
        description: String,
        categoryId: String,
        tags: String,
        language: String,
        ageRating: String,
        coverRes: String,
        initialChapterTitle: String,
        initialChapterContent: String
    ) {
        val user = currentUser.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val storyId = repository.insertStory(
                StoryEntity(
                    authorId = user.id,
                    authorName = user.displayName,
                    title = title.trim(),
                    description = description.trim(),
                    categoryId = categoryId,
                    tags = tags.trim(),
                    language = language,
                    ageRating = ageRating,
                    coverResName = coverRes,
                    status = "ONGOING",
                    chaptersCount = 1,
                    isPublished = true
                )
            )
            repository.insertChapter(
                ChapterEntity(
                    storyId = storyId,
                    chapterNumber = 1,
                    title = initialChapterTitle.ifBlank { "الفصل الأول" },
                    content = initialChapterContent.ifBlank { "اكتب محتوى الفصل هنا..." },
                    wordCount = initialChapterContent.split("\\s+".toRegex()).size,
                    isDraft = false
                )
            )
            // Increment user stories count
            repository.updateUser(user.copy(storiesCount = user.storiesCount + 1))
            emitMessage(if (_isArabic.value) "تم نشر القصة بنجاح! 🎉" else "Story created successfully! 🎉")
            _selectedStoryId.value = storyId
            navigateTo(Screen.WriteStudio)
        }
    }

    fun addNewChapter(storyId: Long, title: String, content: String, isDraft: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            val existingChapters = repository.getChaptersForStory(storyId).first()
            val nextNumber = existingChapters.size + 1
            repository.insertChapter(
                ChapterEntity(
                    storyId = storyId,
                    chapterNumber = nextNumber,
                    title = title.trim(),
                    content = content.trim(),
                    wordCount = content.split("\\s+".toRegex()).size,
                    isDraft = isDraft
                )
            )
            val story = repository.getStoryByIdSync(storyId)
            if (story != null) {
                repository.updateStory(story.copy(chaptersCount = story.chaptersCount + 1, updatedAt = System.currentTimeMillis()))
            }
            emitMessage(if (_isArabic.value) (if (isDraft) "تم حفظ المسودة" else "تم نشر الفصل الجديد!") else (if (isDraft) "Draft saved" else "Chapter published!"))
        }
    }

    // Admin & Moderation Operations
    fun toggleUserBan(user: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setUserBlocked(user.id, !user.isBlocked)
            emitMessage(if (_isArabic.value) (if (user.isBlocked) "تم إلغاء حظر المستخدم" else "تم حظر المستخدم") else "User status updated")
        }
    }

    fun changeUserRole(userId: Long, newRole: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setUserRole(userId, newRole)
            emitMessage(if (_isArabic.value) "تم تغيير دور المستخدم إلى $newRole" else "User role changed to $newRole")
        }
    }

    fun toggleStoryFeatured(story: StoryEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setStoryFeatured(story.id, !story.isFeatured)
            emitMessage(if (_isArabic.value) (if (story.isFeatured) "تمت إزالة القصة من المميزة" else "تم تمييز القصة في الصفحة الرئيسية") else "Featured status updated")
        }
    }

    fun deleteStory(storyId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.softDeleteStory(storyId)
            emitMessage(if (_isArabic.value) "تم حذف القصة بنجاح" else "Story deleted successfully")
        }
    }

    fun resolveReport(reportId: Long, action: String = "RESOLVED") {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateReportStatus(reportId, action)
            emitMessage(if (_isArabic.value) "تم معالجة البلاغ بنجاح" else "Report updated successfully")
        }
    }

    fun submitReport(targetType: String, targetId: Long, targetTitle: String, reason: String) {
        val user = currentUser.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            repository.submitReport(
                ReportEntity(
                    reporterId = user.id,
                    reporterName = user.displayName,
                    targetType = targetType,
                    targetId = targetId,
                    targetTitle = targetTitle,
                    reason = reason
                )
            )
            emitMessage(if (_isArabic.value) "تم إرسال البلاغ إلى فريق الإشراف" else "Report submitted to moderation")
        }
    }

    private fun emitMessage(msg: String) {
        viewModelScope.launch {
            _snackBarMessage.emit(msg)
        }
    }
}
