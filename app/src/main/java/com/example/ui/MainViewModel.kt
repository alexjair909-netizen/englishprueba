package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.PronunciationEvaluator
import com.example.audio.SpeechManager
import com.example.data.local.AppDatabase
import com.example.data.model.Lesson
import com.example.data.model.LessonProgressEntity
import com.example.data.model.LessonWord
import com.example.data.model.PronunciationExercise
import com.example.data.model.PronunciationPracticeEntity
import com.example.data.model.PronunciationResult
import com.example.data.model.SavedWordEntity
import com.example.data.model.UserStatsEntity
import com.example.data.repository.EnglishLearningRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class NavigationTab {
    LESSONS,
    PRONUNCIATION,
    VOCABULARY,
    PROGRESS
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EnglishLearningRepository
    val speechManager: SpeechManager
    private val geminiService = com.example.data.api.GeminiLessonService()

    // Gemini Lesson State
    private val _geminiLesson = MutableStateFlow<com.example.data.api.GeminiGeneratedLesson?>(null)
    val geminiLesson: StateFlow<com.example.data.api.GeminiGeneratedLesson?> = _geminiLesson.asStateFlow()

    private val _isLoadingGeminiLesson = MutableStateFlow(false)
    val isLoadingGeminiLesson: StateFlow<Boolean> = _isLoadingGeminiLesson.asStateFlow()

    private val _selectedGeminiTopic = MutableStateFlow("Coffee Culture & Daily Routines")
    val selectedGeminiTopic: StateFlow<String> = _selectedGeminiTopic.asStateFlow()

    private val _selectedGeminiLevel = MutableStateFlow("A2")
    val selectedGeminiLevel: StateFlow<String> = _selectedGeminiLevel.asStateFlow()

    private val _isFetchingWordDefinition = MutableStateFlow(false)
    val isFetchingWordDefinition: StateFlow<Boolean> = _isFetchingWordDefinition.asStateFlow()

    // ReadingView Speech Recognition & Pronunciation Feedback
    private val _activeReadingPracticeParagraph = MutableStateFlow<Int?>(null)
    val activeReadingPracticeParagraph: StateFlow<Int?> = _activeReadingPracticeParagraph.asStateFlow()

    private val _readingPronunciationResults = MutableStateFlow<Map<Int, PronunciationResult>>(emptyMap())
    val readingPronunciationResults: StateFlow<Map<Int, PronunciationResult>> = _readingPronunciationResults.asStateFlow()

    // UI Tab Navigation
    private val _currentTab = MutableStateFlow(NavigationTab.LESSONS)
    val currentTab: StateFlow<NavigationTab> = _currentTab.asStateFlow()

    // Level Filter for Lessons (All, A1, A2, B1, B2)
    private val _selectedLessonLevel = MutableStateFlow("Todos")
    val selectedLessonLevel: StateFlow<String> = _selectedLessonLevel.asStateFlow()

    // Active Lesson & Reading mode
    private val _activeLesson = MutableStateFlow<Lesson?>(null)
    val activeLesson: StateFlow<Lesson?> = _activeLesson.asStateFlow()

    private val _showSpanishTranslation = MutableStateFlow(true)
    val showSpanishTranslation: StateFlow<Boolean> = _showSpanishTranslation.asStateFlow()

    // Active Quiz inside Lesson
    private val _isQuizActive = MutableStateFlow(false)
    val isQuizActive: StateFlow<Boolean> = _isQuizActive.asStateFlow()

    private val _quizQuestionIndex = MutableStateFlow(0)
    val quizQuestionIndex: StateFlow<Int> = _quizQuestionIndex.asStateFlow()

    private val _quizSelectedAnswer = MutableStateFlow<Int?>(null)
    val quizSelectedAnswer: StateFlow<Int?> = _quizSelectedAnswer.asStateFlow()

    private val _quizCorrectAnswersCount = MutableStateFlow(0)
    val quizCorrectAnswersCount: StateFlow<Int> = _quizCorrectAnswersCount.asStateFlow()

    private val _showQuizCompletedDialog = MutableStateFlow(false)
    val showQuizCompletedDialog: StateFlow<Boolean> = _showQuizCompletedDialog.asStateFlow()

    // Pronunciation Studio
    private val _selectedPronunciationCategory = MutableStateFlow("Todos")
    val selectedPronunciationCategory: StateFlow<String> = _selectedPronunciationCategory.asStateFlow()

    private val _activePronunciationExercise = MutableStateFlow<PronunciationExercise?>(null)
    val activePronunciationExercise: StateFlow<PronunciationExercise?> = _activePronunciationExercise.asStateFlow()

    private val _ttsSpeed = MutableStateFlow(1.0f)
    val ttsSpeed: StateFlow<Float> = _ttsSpeed.asStateFlow()

    private val _lastPronunciationResult = MutableStateFlow<PronunciationResult?>(null)
    val lastPronunciationResult: StateFlow<PronunciationResult?> = _lastPronunciationResult.asStateFlow()

    // Word Lookup Popup / Bottom Sheet
    private val _activeWordDetail = MutableStateFlow<LessonWord?>(null)
    val activeWordDetail: StateFlow<LessonWord?> = _activeWordDetail.asStateFlow()

    private val _toastNotification = MutableStateFlow<String?>(null)
    val toastNotification: StateFlow<String?> = _toastNotification.asStateFlow()

    // Vocabulary Section
    private val _vocabSearchQuery = MutableStateFlow("")
    val vocabSearchQuery: StateFlow<String> = _vocabSearchQuery.asStateFlow()

    private val _isFlashcardMode = MutableStateFlow(false)
    val isFlashcardMode: StateFlow<Boolean> = _isFlashcardMode.asStateFlow()

    private val _currentFlashcardIndex = MutableStateFlow(0)
    val currentFlashcardIndex: StateFlow<Int> = _currentFlashcardIndex.asStateFlow()

    private val _isFlashcardFlipped = MutableStateFlow(false)
    val isFlashcardFlipped: StateFlow<Boolean> = _isFlashcardFlipped.asStateFlow()

    private val _showAddWordDialog = MutableStateFlow(false)
    val showAddWordDialog: StateFlow<Boolean> = _showAddWordDialog.asStateFlow()

    // Repository Flows
    val lessons: List<Lesson>
    val pronunciationExercises: List<PronunciationExercise>

    val lessonProgress: StateFlow<List<LessonProgressEntity>>
    val pronunciationHistory: StateFlow<List<PronunciationPracticeEntity>>
    val savedWords: StateFlow<List<SavedWordEntity>>
    val userStats: StateFlow<UserStatsEntity?>

    init {
        val db = AppDatabase.getInstance(application)
        repository = EnglishLearningRepository(db.appDao())
        speechManager = SpeechManager(application)

        lessons = repository.allLessons
        pronunciationExercises = repository.allPronunciationExercises
        _activePronunciationExercise.value = pronunciationExercises.firstOrNull()

        lessonProgress = repository.lessonProgressFlow.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        pronunciationHistory = repository.pronunciationPracticeFlow.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        savedWords = repository.savedWordsFlow.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        userStats = repository.userStatsFlow.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

        viewModelScope.launch {
            repository.initDatabase()
            loadGeminiLesson()
        }
    }

    fun setTab(tab: NavigationTab) {
        _currentTab.value = tab
    }

    fun setLessonLevel(level: String) {
        _selectedLessonLevel.value = level
    }

    fun openLesson(lesson: Lesson) {
        _activeLesson.value = lesson
        _isQuizActive.value = false
        _quizQuestionIndex.value = 0
        _quizSelectedAnswer.value = null
        _quizCorrectAnswersCount.value = 0
        _showQuizCompletedDialog.value = false
    }

    fun closeLesson() {
        _activeLesson.value = null
        _isQuizActive.value = false
        _showQuizCompletedDialog.value = false
    }

    fun toggleSpanishTranslation() {
        _showSpanishTranslation.value = !_showSpanishTranslation.value
    }

    fun startQuiz() {
        _isQuizActive.value = true
        _quizQuestionIndex.value = 0
        _quizSelectedAnswer.value = null
        _quizCorrectAnswersCount.value = 0
        _showQuizCompletedDialog.value = false
    }

    fun selectQuizAnswer(index: Int) {
        if (_quizSelectedAnswer.value != null) return // already answered
        _quizSelectedAnswer.value = index

        val lesson = _activeLesson.value ?: return
        val currentQ = lesson.quizQuestions.getOrNull(_quizQuestionIndex.value) ?: return

        if (index == currentQ.correctIndex) {
            _quizCorrectAnswersCount.value += 1
        }
    }

    fun nextQuizQuestion() {
        val lesson = _activeLesson.value ?: return
        val nextIdx = _quizQuestionIndex.value + 1
        if (nextIdx < lesson.quizQuestions.size) {
            _quizQuestionIndex.value = nextIdx
            _quizSelectedAnswer.value = null
        } else {
            // Completed quiz!
            val totalQuestions = lesson.quizQuestions.size
            val scorePercent = if (totalQuestions > 0) (_quizCorrectAnswersCount.value * 100) / totalQuestions else 100
            val xpToAward = if (scorePercent >= 60) lesson.xpReward else (lesson.xpReward / 2)

            viewModelScope.launch {
                repository.completeLesson(lesson.id, scorePercent, xpToAward)
            }
            _showQuizCompletedDialog.value = true
        }
    }

    fun dismissQuizDialog() {
        _showQuizCompletedDialog.value = false
        _isQuizActive.value = false
        _activeLesson.value = null
    }

    // Pronunciation Actions
    fun setPronunciationCategory(cat: String) {
        _selectedPronunciationCategory.value = cat
        val filtered = if (cat == "Todos") pronunciationExercises else pronunciationExercises.filter { it.category == cat }
        _activePronunciationExercise.value = filtered.firstOrNull()
        _lastPronunciationResult.value = null
    }

    fun selectPronunciationExercise(ex: PronunciationExercise) {
        _activePronunciationExercise.value = ex
        _lastPronunciationResult.value = null
        speechManager.stopSpeaking()
        speechManager.stopListening()
    }

    fun toggleTtsSpeed() {
        _ttsSpeed.value = if (_ttsSpeed.value == 1.0f) 0.75f else 1.0f
    }

    fun playActiveExerciseAudio() {
        val phrase = _activePronunciationExercise.value?.phrase ?: return
        speechManager.speak(phrase, _ttsSpeed.value)
    }

    fun playWordAudio(word: String) {
        speechManager.speak(word, 0.9f)
    }

    fun startSpeakingPractice() {
        val exercise = _activePronunciationExercise.value ?: return
        _lastPronunciationResult.value = null

        speechManager.startListening { spoken ->
            evaluatePronunciation(exercise, spoken)
        }
    }

    fun simulateSpeakingPractice(sampleSpoken: String? = null) {
        val exercise = _activePronunciationExercise.value ?: return
        val testSpoken = sampleSpoken ?: exercise.phrase
        evaluatePronunciation(exercise, testSpoken)
    }

    private fun evaluatePronunciation(exercise: PronunciationExercise, spoken: String) {
        val result = PronunciationEvaluator.evaluate(exercise.phrase, spoken)
        _lastPronunciationResult.value = result

        viewModelScope.launch {
            repository.recordPronunciationPractice(exercise, result)
        }
    }

    // Word Lookup Details
    fun showWordDetail(word: LessonWord) {
        _activeWordDetail.value = word
    }

    fun dismissWordDetail() {
        _activeWordDetail.value = null
    }

    fun saveWordFromLesson(word: LessonWord) {
        viewModelScope.launch {
            val saved = repository.saveLessonWord(word)
            _toastNotification.value = if (saved) {
                "\"${word.word}\" guardada en tu Vocabulario."
            } else {
                "\"${word.word}\" ya estaba guardada."
            }
        }
    }

    fun addCustomWord(word: String, phonetic: String, translation: String, example: String) {
        viewModelScope.launch {
            repository.saveCustomWord(word, phonetic, translation, example)
            _showAddWordDialog.value = false
            _toastNotification.value = "Palabra guardada con éxito."
        }
    }

    fun toggleWordMastered(word: SavedWordEntity) {
        viewModelScope.launch {
            repository.toggleWordMastered(word)
        }
    }

    fun deleteWord(id: Long) {
        viewModelScope.launch {
            repository.deleteSavedWord(id)
            _toastNotification.value = "Palabra eliminada."
        }
    }

    fun setVocabSearch(query: String) {
        _vocabSearchQuery.value = query
    }

    fun openFlashcardMode() {
        _isFlashcardMode.value = true
        _currentFlashcardIndex.value = 0
        _isFlashcardFlipped.value = false
    }

    fun closeFlashcardMode() {
        _isFlashcardMode.value = false
    }

    fun flipFlashcard() {
        _isFlashcardFlipped.value = !_isFlashcardFlipped.value
    }

    fun nextFlashcard(totalCount: Int) {
        if (totalCount > 0) {
            _currentFlashcardIndex.value = (_currentFlashcardIndex.value + 1) % totalCount
            _isFlashcardFlipped.value = false
        }
    }

    fun prevFlashcard(totalCount: Int) {
        if (totalCount > 0) {
            _currentFlashcardIndex.value = if (_currentFlashcardIndex.value > 0) _currentFlashcardIndex.value - 1 else totalCount - 1
            _isFlashcardFlipped.value = false
        }
    }

    fun setShowAddWordDialog(show: Boolean) {
        _showAddWordDialog.value = show
    }

    fun clearToast() {
        _toastNotification.value = null
    }

    fun setGeminiTopic(topic: String) {
        _selectedGeminiTopic.value = topic
    }

    fun setGeminiLevel(level: String) {
        _selectedGeminiLevel.value = level
    }

    fun loadGeminiLesson(
        topic: String = _selectedGeminiTopic.value,
        level: String = _selectedGeminiLevel.value
    ) {
        viewModelScope.launch {
            _isLoadingGeminiLesson.value = true
            val result = geminiService.generateLesson(topic, level)
            result.onSuccess { lesson ->
                _geminiLesson.value = lesson
            }.onFailure {
                _toastNotification.value = "Error al conectar con Gemini API"
            }
            _isLoadingGeminiLesson.value = false
        }
    }

    fun onInteractiveWordClicked(rawWord: String, contextSentence: String) {
        val cleanWord = rawWord.trim().trim(',', '.', '!', '?', ';', ':', '"', '“', '”', '(', ')')
        if (cleanWord.isBlank()) return

        // Speak word aloud immediately for authentic pronunciation
        speechManager.speak(cleanWord, 0.9f)

        // Check if pre-existing in current lesson vocabulary
        val vocabMatch = _geminiLesson.value?.vocabulary?.find {
            it.word.equals(cleanWord, ignoreCase = true)
        }

        if (vocabMatch != null) {
            _activeWordDetail.value = vocabMatch
        } else {
            // Check other lessons
            val existingLessonWord = lessons.flatMap { it.vocabulary }.find {
                it.word.equals(cleanWord, ignoreCase = true)
            }

            if (existingLessonWord != null) {
                _activeWordDetail.value = existingLessonWord
            } else {
                // Fetch live definition from Gemini
                viewModelScope.launch {
                    _isFetchingWordDefinition.value = true
                    val defResult = geminiService.defineWord(cleanWord, contextSentence)
                    defResult.onSuccess { definedWord ->
                        _activeWordDetail.value = definedWord
                    }
                    _isFetchingWordDefinition.value = false
                }
            }
        }
    }

    fun startParagraphSpeakingPractice(paragraphIndex: Int, targetText: String) {
        _activeReadingPracticeParagraph.value = paragraphIndex
        speechManager.startListening { spoken ->
            evaluateParagraphPronunciation(paragraphIndex, targetText, spoken)
        }
    }

    fun stopParagraphSpeakingPractice() {
        speechManager.stopListening()
        _activeReadingPracticeParagraph.value = null
    }

    fun simulateParagraphSpeakingPractice(paragraphIndex: Int, targetText: String, sampleSpoken: String? = null) {
        _activeReadingPracticeParagraph.value = null
        val spoken = sampleSpoken ?: targetText
        evaluateParagraphPronunciation(paragraphIndex, targetText, spoken)
    }

    private fun evaluateParagraphPronunciation(paragraphIndex: Int, targetText: String, spoken: String) {
        val result = PronunciationEvaluator.evaluate(targetText, spoken)
        _readingPronunciationResults.value = _readingPronunciationResults.value + (paragraphIndex to result)
        _activeReadingPracticeParagraph.value = null

        viewModelScope.launch {
            val ex = PronunciationExercise(
                id = "reading_p_${paragraphIndex}_${System.currentTimeMillis()}",
                phrase = targetText,
                phonetic = "",
                translation = "Lectura interactiva en ReadingView",
                level = _selectedGeminiLevel.value,
                category = "Lectura en Voz Alta",
                pronunciationTip = result.feedbackSummary
            )
            repository.recordPronunciationPractice(ex, result)
        }
    }

    fun clearParagraphPronunciationResult(paragraphIndex: Int) {
        _readingPronunciationResults.value = _readingPronunciationResults.value - paragraphIndex
    }

    override fun onCleared() {
        super.onCleared()
        speechManager.shutdown()
    }
}
