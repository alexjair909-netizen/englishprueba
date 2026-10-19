package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lesson_progress")
data class LessonProgressEntity(
    @PrimaryKey val lessonId: String,
    val isCompleted: Boolean,
    val score: Int,
    val completionDate: Long
)

@Entity(tableName = "pronunciation_practice")
data class PronunciationPracticeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val phrase: String,
    val recognizedText: String,
    val score: Int,
    val date: Long = System.currentTimeMillis(),
    val category: String = ""
)

@Entity(tableName = "saved_words")
data class SavedWordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val word: String,
    val phonetic: String,
    val translation: String,
    val example: String = "",
    val isMastered: Boolean = false,
    val dateAdded: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_stats")
data class UserStatsEntity(
    @PrimaryKey val id: Int = 1,
    val xp: Int = 120,
    val streak: Int = 3,
    val lastActiveDate: Long = System.currentTimeMillis(),
    val dailyGoalXp: Int = 50,
    val totalWordsPracticed: Int = 18,
    val totalPronunciationsDone: Int = 8
)
