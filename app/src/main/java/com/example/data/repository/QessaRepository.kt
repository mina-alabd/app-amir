package com.example.data.repository

import com.example.data.local.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

class QessaRepository(private val db: QessaDatabase) {

    private val userDao = db.userDao()
    private val storyDao = db.storyDao()
    private val chapterDao = db.chapterDao()
    private val socialDao = db.socialDao()

    // Users & Auth
    fun getAllUsers(): Flow<List<UserEntity>> = userDao.getAllUsers()
    fun getUserById(userId: Long): Flow<UserEntity?> = userDao.getUserById(userId)
    suspend fun getUserByIdSync(userId: Long): UserEntity? = userDao.getUserByIdSync(userId)
    suspend fun getUserByEmail(email: String): UserEntity? = userDao.getUserByEmail(email)
    suspend fun getUserByUsername(username: String): UserEntity? = userDao.getUserByUsername(username)
    suspend fun insertUser(user: UserEntity): Long = userDao.insertUser(user)
    suspend fun updateUser(user: UserEntity) = userDao.updateUser(user)
    suspend fun setUserBlocked(userId: Long, blocked: Boolean) = userDao.setUserBlocked(userId, blocked)
    suspend fun setUserRole(userId: Long, role: String) = userDao.setUserRole(userId, role)
    suspend fun deleteUser(userId: Long) = userDao.deleteUser(userId)
    fun getTopAuthors(): Flow<List<UserEntity>> = userDao.getTopAuthors()

    // Stories
    fun getAllPublishedStories(): Flow<List<StoryEntity>> = storyDao.getAllPublishedStories()
    fun getStoryById(storyId: Long): Flow<StoryEntity?> = storyDao.getStoryById(storyId)
    suspend fun getStoryByIdSync(storyId: Long): StoryEntity? = storyDao.getStoryByIdSync(storyId)
    fun getStoriesByAuthor(authorId: Long): Flow<List<StoryEntity>> = storyDao.getStoriesByAuthor(authorId)
    fun getStoriesByCategory(categoryId: String): Flow<List<StoryEntity>> = storyDao.getStoriesByCategory(categoryId)
    fun getFeaturedStories(): Flow<List<StoryEntity>> = storyDao.getFeaturedStories()
    fun getTrendingStories(): Flow<List<StoryEntity>> = storyDao.getTrendingStories()
    fun getTopRatedStories(): Flow<List<StoryEntity>> = storyDao.getTopRatedStories()
    fun getNewArrivals(): Flow<List<StoryEntity>> = storyDao.getNewArrivals()
    fun searchStories(query: String): Flow<List<StoryEntity>> = storyDao.searchStories(query)
    suspend fun insertStory(story: StoryEntity): Long = storyDao.insertStory(story)
    suspend fun updateStory(story: StoryEntity) = storyDao.updateStory(story)
    suspend fun incrementReads(storyId: Long) = storyDao.incrementReads(storyId)
    suspend fun updateStoryLikes(storyId: Long, delta: Int) = storyDao.updateLikes(storyId, delta)
    suspend fun softDeleteStory(storyId: Long) = storyDao.softDeleteStory(storyId)
    suspend fun setStoryFeatured(storyId: Long, featured: Boolean) = storyDao.setStoryFeatured(storyId, featured)
    fun getStoriesCount(): Flow<Int> = storyDao.getStoriesCount()
    fun getTotalReads(): Flow<Int?> = storyDao.getTotalReads()

    // Chapters
    fun getChaptersForStory(storyId: Long): Flow<List<ChapterEntity>> = chapterDao.getChaptersForStory(storyId)
    fun getPublishedChaptersForStory(storyId: Long): Flow<List<ChapterEntity>> = chapterDao.getPublishedChaptersForStory(storyId)
    fun getChapterById(chapterId: Long): Flow<ChapterEntity?> = chapterDao.getChapterById(chapterId)
    suspend fun getChapterByIdSync(chapterId: Long): ChapterEntity? = chapterDao.getChapterByIdSync(chapterId)
    suspend fun insertChapter(chapter: ChapterEntity): Long = chapterDao.insertChapter(chapter)
    suspend fun updateChapter(chapter: ChapterEntity) = chapterDao.updateChapter(chapter)
    suspend fun deleteChapter(chapterId: Long) = chapterDao.deleteChapter(chapterId)
    suspend fun incrementChapterReads(chapterId: Long) = chapterDao.incrementChapterReads(chapterId)
    suspend fun updateChapterLikes(chapterId: Long, delta: Int) = chapterDao.updateChapterLikes(chapterId, delta)
    fun getTotalChaptersCount(): Flow<Int> = chapterDao.getTotalChaptersCount()

    // Categories
    fun getAllCategories(): Flow<List<CategoryEntity>> = socialDao.getAllCategories()
    suspend fun insertCategory(category: CategoryEntity) = socialDao.insertCategory(category)
    suspend fun deleteCategory(category: CategoryEntity) = socialDao.deleteCategory(category)

    // Library
    fun getUserLibraryStoriesByList(userId: Long, listType: String): Flow<List<StoryEntity>> =
        socialDao.getUserLibraryStoriesByList(userId, listType)
    fun getAllUserLibraryStories(userId: Long): Flow<List<StoryEntity>> =
        socialDao.getAllUserLibraryStories(userId)
    fun observeLibraryItem(userId: Long, storyId: Long): Flow<LibraryItemEntity?> =
        socialDao.observeLibraryItem(userId, storyId)
    suspend fun addToLibrary(userId: Long, storyId: Long, listType: String = "CURRENT") {
        socialDao.insertLibraryItem(LibraryItemEntity(userId = userId, storyId = storyId, readingList = listType))
    }
    suspend fun removeFromLibrary(userId: Long, storyId: Long) {
        socialDao.removeLibraryItem(userId, storyId)
    }

    // Reading Progress
    fun getReadingProgress(userId: Long, storyId: Long): Flow<ReadingProgressEntity?> =
        socialDao.getReadingProgress(userId, storyId)
    fun getAllReadingProgress(userId: Long): Flow<List<ReadingProgressEntity>> =
        socialDao.getAllReadingProgress(userId)
    suspend fun saveReadingProgress(userId: Long, storyId: Long, chapterId: Long, chapterNumber: Int, progress: Float) {
        socialDao.saveReadingProgress(
            ReadingProgressEntity(
                userId = userId,
                storyId = storyId,
                lastChapterId = chapterId,
                lastChapterNumber = chapterNumber,
                scrollProgress = progress,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    // Likes
    fun isStoryLiked(userId: Long, storyId: Long): Flow<LikeEntity?> = socialDao.isStoryLiked(userId, storyId)
    fun isChapterLiked(userId: Long, chapterId: Long): Flow<LikeEntity?> = socialDao.isChapterLiked(userId, chapterId)
    suspend fun toggleStoryLike(userId: Long, storyId: Long, isLiked: Boolean) {
        if (isLiked) {
            socialDao.removeStoryLike(userId, storyId)
            storyDao.updateLikes(storyId, -1)
        } else {
            socialDao.insertLike(LikeEntity(userId = userId, storyId = storyId))
            storyDao.updateLikes(storyId, 1)
        }
    }
    suspend fun toggleChapterLike(userId: Long, storyId: Long, chapterId: Long, isLiked: Boolean) {
        if (isLiked) {
            socialDao.removeChapterLike(userId, chapterId)
            chapterDao.updateChapterLikes(chapterId, -1)
        } else {
            socialDao.insertLike(LikeEntity(userId = userId, storyId = storyId, chapterId = chapterId))
            chapterDao.updateChapterLikes(chapterId, 1)
        }
    }

    // Follows
    fun isFollowing(followerId: Long, followingId: Long): Flow<FollowEntity?> = socialDao.isFollowing(followerId, followingId)
    fun getFollowedAuthors(userId: Long): Flow<List<UserEntity>> = socialDao.getFollowedAuthors(userId)
    suspend fun toggleFollow(followerId: Long, followingId: Long, isFollowing: Boolean) {
        if (isFollowing) {
            socialDao.removeFollow(followerId, followingId)
        } else {
            socialDao.insertFollow(FollowEntity(followerId = followerId, followingId = followingId))
            // create notification
            socialDao.insertNotification(
                NotificationEntity(
                    userId = followingId,
                    senderName = "متابع جديد",
                    title = "متابع جديد لقصصك",
                    message = "بدأ مستخدم بمتابعة قلمك وإبداعك!",
                    type = "FOLLOW",
                    targetId = followerId
                )
            )
        }
    }

    // Comments
    fun getStoryComments(storyId: Long): Flow<List<CommentEntity>> = socialDao.getStoryComments(storyId)
    fun getChapterComments(chapterId: Long): Flow<List<CommentEntity>> = socialDao.getChapterComments(chapterId)
    suspend fun addComment(comment: CommentEntity) = socialDao.insertComment(comment)
    suspend fun deleteComment(commentId: Long) = socialDao.deleteComment(commentId)
    fun getTotalCommentsCount(): Flow<Int> = socialDao.getTotalCommentsCount()

    // Ratings
    fun getStoryRatings(storyId: Long): Flow<List<RatingEntity>> = socialDao.getStoryRatings(storyId)
    fun getUserRatingForStory(userId: Long, storyId: Long): Flow<RatingEntity?> = socialDao.getUserRatingForStory(userId, storyId)
    suspend fun submitRating(rating: RatingEntity) = socialDao.insertRating(rating)

    // Notifications
    fun getUserNotifications(userId: Long): Flow<List<NotificationEntity>> = socialDao.getUserNotifications(userId)
    fun getUnreadNotificationsCount(userId: Long): Flow<Int> = socialDao.getUnreadNotificationsCount(userId)
    suspend fun markNotificationRead(id: Long) = socialDao.markNotificationRead(id)
    suspend fun markAllNotificationsRead(userId: Long) = socialDao.markAllNotificationsRead(userId)

    // Reports (Admin / Moderation)
    fun getAllReports(): Flow<List<ReportEntity>> = socialDao.getAllReports()
    fun getPendingReports(): Flow<List<ReportEntity>> = socialDao.getPendingReports()
    suspend fun submitReport(report: ReportEntity) = socialDao.insertReport(report)
    suspend fun updateReportStatus(reportId: Long, status: String) = socialDao.updateReportStatus(reportId, status)
}
