package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val displayName: String,
    val email: String,
    val password: String = "password123",
    val bio: String = "",
    val avatarUrl: String = "",
    val role: String = "READER", // READER, WRITER, MODERATOR, ADMIN
    val isBlocked: Boolean = false,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val storiesCount: Int = 0,
    val readsReceived: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "stories")
data class StoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val authorId: Long,
    val authorName: String,
    val authorAvatar: String = "",
    val title: String,
    val description: String,
    val coverResName: String = "cover_desert_stars",
    val categoryId: String,
    val tags: String = "", // Comma separated
    val language: String = "ar", // "ar" or "en"
    val ageRating: String = "ALL", // ALL, TEEN, MATURE
    val status: String = "ONGOING", // ONGOING, COMPLETED, ON_HOLD
    val readsCount: Int = 0,
    val likesCount: Int = 0,
    val ratingAvg: Float = 4.8f,
    val ratingsCount: Int = 0,
    val chaptersCount: Int = 1,
    val isPublished: Boolean = true,
    val isFeatured: Boolean = false,
    val isEditorChoice: Boolean = false,
    val isDeleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "chapters")
data class ChapterEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val storyId: Long,
    val chapterNumber: Int,
    val title: String,
    val content: String,
    val isDraft: Boolean = false,
    val wordCount: Int = 0,
    val readsCount: Int = 0,
    val likesCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: String,
    val nameAr: String,
    val nameEn: String,
    val iconName: String = "Book",
    val description: String = "",
    val storyCount: Int = 0
)

@Entity(tableName = "library_items")
data class LibraryItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val storyId: Long,
    val readingList: String = "CURRENT", // CURRENT, FAVORITES, LATER, FINISHED
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "reading_progress")
data class ReadingProgressEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val storyId: Long,
    val lastChapterId: Long,
    val lastChapterNumber: Int,
    val scrollProgress: Float = 0f,
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "likes")
data class LikeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val storyId: Long,
    val chapterId: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "follows")
data class FollowEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val followerId: Long,
    val followingId: Long,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val storyId: Long,
    val chapterId: Long? = null,
    val userId: Long,
    val userName: String,
    val userAvatar: String = "",
    val content: String,
    val likesCount: Int = 0,
    val parentId: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "ratings")
data class RatingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val storyId: Long,
    val stars: Int,
    val review: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val senderName: String,
    val senderAvatar: String = "",
    val title: String,
    val message: String,
    val type: String = "SYSTEM", // FOLLOW, LIKE, COMMENT, CHAPTER_UPDATE, SYSTEM
    val targetId: Long? = null,
    val isRead: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val reporterId: Long,
    val reporterName: String,
    val targetType: String, // STORY, COMMENT, USER
    val targetId: Long,
    val targetTitle: String,
    val reason: String,
    val status: String = "PENDING", // PENDING, RESOLVED, DISMISSED
    val createdAt: Long = System.currentTimeMillis()
)
