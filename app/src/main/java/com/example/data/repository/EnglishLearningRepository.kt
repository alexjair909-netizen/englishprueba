package com.example.data.repository

import com.example.audio.PronunciationEvaluator
import com.example.data.local.AppDao
import com.example.data.model.Lesson
import com.example.data.model.LessonProgressEntity
import com.example.data.model.LessonWord
import com.example.data.model.PronunciationExercise
import com.example.data.model.PronunciationPracticeEntity
import com.example.data.model.PronunciationResult
import com.example.data.model.SavedWordEntity
import com.example.data.model.UserStatsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlin.math.roundToInt

class EnglishLearningRepository(private val appDao: AppDao) {

    val allLessons: List<Lesson> = CurriculumDataSource.lessons
    val allPronunciationExercises: List<PronunciationExercise> = CurriculumDataSource.pronunciationExercises

    val lessonProgressFlow: Flow<List<LessonProgressEntity>> = appDao.getAllLessonProgress()
    val pronunciationPracticeFlow: Flow<List<PronunciationPracticeEntity>> = appDao.getAllPronunciationPractice()
    val savedWordsFlow: Flow<List<SavedWordEntity>> = appDao.getAllSavedWords()
    val userStatsFlow: Flow<UserStatsEntity?> = appDao.getUserStats()

    suspend fun initDatabase() {
        // Seed user stats if empty
        val currentStats = appDao.getUserStats().firstOrNull()
        if (currentStats == null) {
            appDao.insertOrUpdateUserStats(
                UserStatsEntity(
                    id = 1,
                    xp = 180,
                    streak = 4,
                    lastActiveDate = System.currentTimeMillis(),
                    dailyGoalXp = 50,
                    totalWordsPracticed = 24,
                    totalPronunciationsDone = 12
                )
            )

            // Seed a few starter saved words
            val initialWords = listOf(
                SavedWordEntity(
                    word = "peaceful",
                    phonetic = "/ˈpiːs.fəl/",
                    translation = "Pacífico, sereno",
                    example = "Sunday mornings are very peaceful.",
                    isMastered = true
                ),
                SavedWordEntity(
                    word = "enthusiastically",
                    phonetic = "/ɪnˌθuː.ziˈæs.tɪ.kəl.i/",
                    translation = "Con entusiasmo",
                    example = "She agreed enthusiastically with the proposal.",
                    isMastered = false
                ),
                SavedWordEntity(
                    word = "ecosystem",
                    phonetic = "/ˈiː.koʊˌsɪs.təm/",
                    translation = "Ecosistema",
                    example = "A digital ecosystem connects developers worldwide.",
                    isMastered = false
                ),
                SavedWordEntity(
                    word = "paramount",
                    phonetic = "/ˈpær.ə.maʊnt/",
                    translation = "Primordial, de suma importancia",
                    example = "Clear communication is paramount in team projects.",
                    isMastered = false
                )
            )
            for (w in initialWords) {
                appDao.insertSavedWord(w)
            }

            // Mark one lesson as completed for initial progress demo
            appDao.insertOrUpdateLessonProgress(
                LessonProgressEntity(
                    lessonId = "lesson_a1_morning",
                    isCompleted = true,
                    score = 100,
                    completionDate = System.currentTimeMillis() - 86400000L
                )
            )

            // Seed initial pronunciation logs
            appDao.insertPronunciationPractice(
                PronunciationPracticeEntity(
                    phrase = "Nice to meet you, I am learning English.",
                    recognizedText = "Nice to meet you, I am learning English.",
                    score = 96,
                    date = System.currentTimeMillis() - 86400000L,
                    category = "Frases Esenciales"
                )
            )
            appDao.insertPronunciationPractice(
                PronunciationPracticeEntity(
                    phrase = "Where is the nearest subway station?",
                    recognizedText = "Where is the nearest subway station?",
                    score = 88,
                    date = System.currentTimeMillis() - 43200000L,
                    category = "Viajes y Restaurantes"
                )
            )
        }
    }

    suspend fun completeLesson(lessonId: String, quizScore: Int, xpEarned: Int) {
        val progress = LessonProgressEntity(
            lessonId = lessonId,
            isCompleted = true,
            score = quizScore,
            completionDate = System.currentTimeMillis()
        )
        appDao.insertOrUpdateLessonProgress(progress)

        // Add XP to user stats
        val stats = appDao.getUserStats().firstOrNull() ?: UserStatsEntity(id = 1)
        val updatedStats = stats.copy(
            xp = stats.xp + xpEarned,
            lastActiveDate = System.currentTimeMillis(),
            totalWordsPracticed = stats.totalWordsPracticed + 5
        )
        appDao.insertOrUpdateUserStats(updatedStats)
    }

    suspend fun recordPronunciationPractice(
        exercise: PronunciationExercise,
        result: PronunciationResult
    ) {
        appDao.insertPronunciationPractice(
            PronunciationPracticeEntity(
                phrase = exercise.phrase,
                recognizedText = result.recognizedText,
                score = result.overallScore,
                date = System.currentTimeMillis(),
                category = exercise.category
            )
        )

        val xpGained = when {
            result.overallScore >= 90 -> 25
            result.overallScore >= 75 -> 18
            result.overallScore >= 50 -> 10
            else -> 5
        }

        val stats = appDao.getUserStats().firstOrNull() ?: UserStatsEntity(id = 1)
        val updatedStats = stats.copy(
            xp = stats.xp + xpGained,
            lastActiveDate = System.currentTimeMillis(),
            totalPronunciationsDone = stats.totalPronunciationsDone + 1,
            totalWordsPracticed = stats.totalWordsPracticed + result.wordEvaluations.size
        )
        appDao.insertOrUpdateUserStats(updatedStats)
    }

    suspend fun saveLessonWord(word: LessonWord): Boolean {
        val count = appDao.isWordSaved(word.word)
        if (count > 0) return false // already saved
        appDao.insertSavedWord(
            SavedWordEntity(
                word = word.word,
                phonetic = word.phonetic,
                translation = word.translation,
                example = word.exampleSentence,
                isMastered = false,
                dateAdded = System.currentTimeMillis()
            )
        )
        return true
    }

    suspend fun saveCustomWord(word: String, phonetic: String, translation: String, example: String) {
        appDao.insertSavedWord(
            SavedWordEntity(
                word = word.trim(),
                phonetic = phonetic.trim(),
                translation = translation.trim(),
                example = example.trim(),
                isMastered = false,
                dateAdded = System.currentTimeMillis()
            )
        )
    }

    suspend fun toggleWordMastered(word: SavedWordEntity) {
        appDao.updateSavedWord(word.copy(isMastered = !word.isMastered))
    }

    suspend fun deleteSavedWord(id: Long) {
        appDao.deleteSavedWordById(id)
    }
}
