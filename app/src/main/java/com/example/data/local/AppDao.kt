package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.LessonProgressEntity
import com.example.data.model.PronunciationPracticeEntity
import com.example.data.model.SavedWordEntity
import com.example.data.model.UserStatsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // Lesson Progress
    @Query("SELECT * FROM lesson_progress")
    fun getAllLessonProgress(): Flow<List<LessonProgressEntity>>

    @Query("SELECT * FROM lesson_progress WHERE lessonId = :lessonId LIMIT 1")
    suspend fun getLessonProgressById(lessonId: String): LessonProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateLessonProgress(progress: LessonProgressEntity)

    // Pronunciation Practice History
    @Query("SELECT * FROM pronunciation_practice ORDER BY date DESC")
    fun getAllPronunciationPractice(): Flow<List<PronunciationPracticeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPronunciationPractice(practice: PronunciationPracticeEntity): Long

    // Saved Vocabulary
    @Query("SELECT * FROM saved_words ORDER BY dateAdded DESC")
    fun getAllSavedWords(): Flow<List<SavedWordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedWord(word: SavedWordEntity): Long

    @Update
    suspend fun updateSavedWord(word: SavedWordEntity)

    @Query("DELETE FROM saved_words WHERE id = :id")
    suspend fun deleteSavedWordById(id: Long)

    @Query("SELECT COUNT(*) FROM saved_words WHERE LOWER(word) = LOWER(:word)")
    suspend fun isWordSaved(word: String): Int

    // User Stats
    @Query("SELECT * FROM user_stats WHERE id = 1 LIMIT 1")
    fun getUserStats(): Flow<UserStatsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUserStats(stats: UserStatsEntity)
}
