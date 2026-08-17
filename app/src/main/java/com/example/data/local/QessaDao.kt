package com.example.data.local

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY id ASC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun getUserById(userId: Long): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserByIdSync(userId: Long): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET isBlocked = :blocked WHERE id = :userId")
    suspend fun setUserBlocked(userId: Long, blocked: Boolean)

    @Query("UPDATE users SET role = :role WHERE id = :userId")
    suspend fun setUserRole(userId: Long, role: String)

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: Long)

    @Query("SELECT * FROM users WHERE role = 'WRITER' OR storiesCount > 0 ORDER BY followersCount DESC LIMIT 10")
    fun getTopAuthors(): Flow<List<UserEntity>>
}

@Dao
interface StoryDao {
    @Query("SELECT * FROM stories WHERE isDeleted = 0 ORDER BY readsCount DESC")
    fun getAllPublishedStories(): Flow<List<StoryEntity>>

    @Query("SELECT * FROM stories WHERE id = :storyId LIMIT 1")
    fun getStoryById(storyId: Long): Flow<StoryEntity?>

    @Query("SELECT * FROM stories WHERE id = :storyId LIMIT 1")
    suspend fun getStoryByIdSync(storyId: Long): StoryEntity?

    @Query("SELECT * FROM stories WHERE authorId = :authorId AND isDeleted = 0 ORDER BY updatedAt DESC")
    fun getStoriesByAuthor(authorId: Long): Flow<List<StoryEntity>>

    @Query("SELECT * FROM stories WHERE categoryId = :categoryId AND isDeleted = 0 ORDER BY readsCount DESC")
    fun getStoriesByCategory(categoryId: String): Flow<List<StoryEntity>>

    @Query("SELECT * FROM stories WHERE isFeatured = 1 AND isDeleted = 0 ORDER BY updatedAt DESC LIMIT 5")
    fun getFeaturedStories(): Flow<List<StoryEntity>>

    @Query("SELECT * FROM stories WHERE isDeleted = 0 ORDER BY readsCount DESC LIMIT 10")
    fun getTrendingStories(): Flow<List<StoryEntity>>

    @Query("SELECT * FROM stories WHERE isDeleted = 0 ORDER BY ratingAvg DESC, ratingsCount DESC LIMIT 10")
    fun getTopRatedStories(): Flow<List<StoryEntity>>

    @Query("SELECT * FROM stories WHERE isDeleted = 0 ORDER BY createdAt DESC LIMIT 10")
    fun getNewArrivals(): Flow<List<StoryEntity>>

    @Query("""
        SELECT * FROM stories 
        WHERE isDeleted = 0 AND (
            title LIKE '%' || :query || '%' OR 
            description LIKE '%' || :query || '%' OR 
            authorName LIKE '%' || :query || '%' OR 
            tags LIKE '%' || :query || '%'
        )
        ORDER BY readsCount DESC
    """)
    fun searchStories(query: String): Flow<List<StoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: StoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(stories: List<StoryEntity>)

    @Update
    suspend fun updateStory(story: StoryEntity)

    @Query("UPDATE stories SET readsCount = readsCount + 1 WHERE id = :storyId")
    suspend fun incrementReads(storyId: Long)

    @Query("UPDATE stories SET likesCount = likesCount + :delta WHERE id = :storyId")
    suspend fun updateLikes(storyId: Long, delta: Int)

    @Query("UPDATE stories SET isDeleted = 1 WHERE id = :storyId")
    suspend fun softDeleteStory(storyId: Long)

    @Query("UPDATE stories SET isFeatured = :featured WHERE id = :storyId")
    suspend fun setStoryFeatured(storyId: Long, featured: Boolean)

    @Query("SELECT COUNT(*) FROM stories WHERE isDeleted = 0")
    fun getStoriesCount(): Flow<Int>

    @Query("SELECT SUM(readsCount) FROM stories WHERE isDeleted = 0")
    fun getTotalReads(): Flow<Int?>
}

@Dao
interface ChapterDao {
    @Query("SELECT * FROM chapters WHERE storyId = :storyId ORDER BY chapterNumber ASC")
    fun getChaptersForStory(storyId: Long): Flow<List<ChapterEntity>>

    @Query("SELECT * FROM chapters WHERE storyId = :storyId AND isDraft = 0 ORDER BY chapterNumber ASC")
    fun getPublishedChaptersForStory(storyId: Long): Flow<List<ChapterEntity>>

    @Query("SELECT * FROM chapters WHERE id = :chapterId LIMIT 1")
    fun getChapterById(chapterId: Long): Flow<ChapterEntity?>

    @Query("SELECT * FROM chapters WHERE id = :chapterId LIMIT 1")
    suspend fun getChapterByIdSync(chapterId: Long): ChapterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapter(chapter: ChapterEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapters(chapters: List<ChapterEntity>)

    @Update
    suspend fun updateChapter(chapter: ChapterEntity)

    @Query("DELETE FROM chapters WHERE id = :chapterId")
    suspend fun deleteChapter(chapterId: Long)

    @Query("UPDATE chapters SET readsCount = readsCount + 1 WHERE id = :chapterId")
    suspend fun incrementChapterReads(chapterId: Long)

    @Query("UPDATE chapters SET likesCount = likesCount + :delta WHERE id = :chapterId")
    suspend fun updateChapterLikes(chapterId: Long, delta: Int)

    @Query("SELECT COUNT(*) FROM chapters WHERE isDraft = 0")
    fun getTotalChaptersCount(): Flow<Int>
}

@Dao
interface SocialDao {
    // Categories
    @Query("SELECT * FROM categories ORDER BY nameAr ASC")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)

    // Library
    @Query("SELECT s.* FROM stories s INNER JOIN library_items l ON s.id = l.storyId WHERE l.userId = :userId AND l.readingList = :listType ORDER BY l.addedAt DESC")
    fun getUserLibraryStoriesByList(userId: Long, listType: String): Flow<List<StoryEntity>>

    @Query("SELECT s.* FROM stories s INNER JOIN library_items l ON s.id = l.storyId WHERE l.userId = :userId ORDER BY l.addedAt DESC")
    fun getAllUserLibraryStories(userId: Long): Flow<List<StoryEntity>>

    @Query("SELECT * FROM library_items WHERE userId = :userId AND storyId = :storyId LIMIT 1")
    suspend fun getLibraryItem(userId: Long, storyId: Long): LibraryItemEntity?

    @Query("SELECT * FROM library_items WHERE userId = :userId AND storyId = :storyId LIMIT 1")
    fun observeLibraryItem(userId: Long, storyId: Long): Flow<LibraryItemEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLibraryItem(item: LibraryItemEntity)

    @Query("DELETE FROM library_items WHERE userId = :userId AND storyId = :storyId")
    suspend fun removeLibraryItem(userId: Long, storyId: Long)

    // Reading Progress
    @Query("SELECT * FROM reading_progress WHERE userId = :userId AND storyId = :storyId LIMIT 1")
    fun getReadingProgress(userId: Long, storyId: Long): Flow<ReadingProgressEntity?>

    @Query("SELECT * FROM reading_progress WHERE userId = :userId ORDER BY updatedAt DESC")
    fun getAllReadingProgress(userId: Long): Flow<List<ReadingProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveReadingProgress(progress: ReadingProgressEntity)

    // Likes
    @Query("SELECT * FROM likes WHERE userId = :userId AND storyId = :storyId AND chapterId IS NULL LIMIT 1")
    fun isStoryLiked(userId: Long, storyId: Long): Flow<LikeEntity?>

    @Query("SELECT * FROM likes WHERE userId = :userId AND chapterId = :chapterId LIMIT 1")
    fun isChapterLiked(userId: Long, chapterId: Long): Flow<LikeEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLike(like: LikeEntity)

    @Query("DELETE FROM likes WHERE userId = :userId AND storyId = :storyId AND chapterId IS NULL")
    suspend fun removeStoryLike(userId: Long, storyId: Long)

    @Query("DELETE FROM likes WHERE userId = :userId AND chapterId = :chapterId")
    suspend fun removeChapterLike(userId: Long, chapterId: Long)

    // Follows
    @Query("SELECT * FROM follows WHERE followerId = :followerId AND followingId = :followingId LIMIT 1")
    fun isFollowing(followerId: Long, followingId: Long): Flow<FollowEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFollow(follow: FollowEntity)

    @Query("DELETE FROM follows WHERE followerId = :followerId AND followingId = :followingId")
    suspend fun removeFollow(followerId: Long, followingId: Long)

    @Query("SELECT u.* FROM users u INNER JOIN follows f ON u.id = f.followingId WHERE f.followerId = :userId")
    fun getFollowedAuthors(userId: Long): Flow<List<UserEntity>>

    // Comments
    @Query("SELECT * FROM comments WHERE storyId = :storyId AND chapterId IS NULL ORDER BY createdAt DESC")
    fun getStoryComments(storyId: Long): Flow<List<CommentEntity>>

    @Query("SELECT * FROM comments WHERE chapterId = :chapterId ORDER BY createdAt DESC")
    fun getChapterComments(chapterId: Long): Flow<List<CommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: CommentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComments(comments: List<CommentEntity>)

    @Query("DELETE FROM comments WHERE id = :commentId")
    suspend fun deleteComment(commentId: Long)

    @Query("SELECT COUNT(*) FROM comments")
    fun getTotalCommentsCount(): Flow<Int>

    // Ratings
    @Query("SELECT * FROM ratings WHERE storyId = :storyId ORDER BY createdAt DESC")
    fun getStoryRatings(storyId: Long): Flow<List<RatingEntity>>

    @Query("SELECT * FROM ratings WHERE userId = :userId AND storyId = :storyId LIMIT 1")
    fun getUserRatingForStory(userId: Long, storyId: Long): Flow<RatingEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRating(rating: RatingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRatings(ratings: List<RatingEntity>)

    // Notifications
    @Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY createdAt DESC")
    fun getUserNotifications(userId: Long): Flow<List<NotificationEntity>>

    @Query("SELECT COUNT(*) FROM notifications WHERE userId = :userId AND isRead = 0")
    fun getUnreadNotificationsCount(userId: Long): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :notificationId")
    suspend fun markNotificationRead(notificationId: Long)

    @Query("UPDATE notifications SET isRead = 1 WHERE userId = :userId")
    suspend fun markAllNotificationsRead(userId: Long)

    // Reports (Admin / Moderation)
    @Query("SELECT * FROM reports ORDER BY createdAt DESC")
    fun getAllReports(): Flow<List<ReportEntity>>

    @Query("SELECT * FROM reports WHERE status = 'PENDING' ORDER BY createdAt DESC")
    fun getPendingReports(): Flow<List<ReportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: ReportEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReports(reports: List<ReportEntity>)

    @Query("UPDATE reports SET status = :status WHERE id = :reportId")
    suspend fun updateReportStatus(reportId: Long, status: String)
}
