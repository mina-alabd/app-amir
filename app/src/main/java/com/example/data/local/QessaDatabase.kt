package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        StoryEntity::class,
        ChapterEntity::class,
        CategoryEntity::class,
        LibraryItemEntity::class,
        ReadingProgressEntity::class,
        LikeEntity::class,
        FollowEntity::class,
        CommentEntity::class,
        RatingEntity::class,
        NotificationEntity::class,
        ReportEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class QessaDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun storyDao(): StoryDao
    abstract fun chapterDao(): ChapterDao
    abstract fun socialDao(): SocialDao

    companion object {
        @Volatile
        private var INSTANCE: QessaDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): QessaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QessaDatabase::class.java,
                    "qessa_stories_database"
                )
                .addCallback(DatabaseCallback(scope))
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database)
                    }
                }
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        val count = database.userDao().getUserByIdSync(1)
                        if (count == null) {
                            populateInitialData(database)
                        }
                    }
                }
            }

            suspend fun populateInitialData(db: QessaDatabase) {
                db.socialDao().insertCategories(SeedData.categories)
                db.userDao().insertUsers(SeedData.users)
                db.storyDao().insertStories(SeedData.stories)
                db.chapterDao().insertChapters(SeedData.chapters)
                db.socialDao().insertComments(SeedData.comments)
                db.socialDao().insertRatings(SeedData.ratings)
                db.socialDao().insertNotifications(SeedData.notifications)
                SeedData.initialLibraries.forEach { db.socialDao().insertLibraryItem(it) }
                SeedData.initialProgress.forEach { db.socialDao().saveReadingProgress(it) }
                db.socialDao().insertReports(SeedData.reports)
            }
        }
    }
}
