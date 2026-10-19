package com.example.data.model

data class Lesson(
    val id: String,
    val title: String,
    val level: String, // A1, A2, B1, B2
    val category: String, // Cotidiano, Viajes, Trabajo, Cultura, Ciencia
    val estimatedMinutes: Int,
    val xpReward: Int,
    val storyTitle: String,
    val storyIntroduction: String,
    val paragraphs: List<StoryParagraph>,
    val vocabulary: List<LessonWord>,
    val quizQuestions: List<QuizQuestion>
)

data class StoryParagraph(
    val englishText: String,
    val spanishText: String
)

data class LessonWord(
    val word: String,
    val phonetic: String,
    val partOfSpeech: String,
    val translation: String,
    val exampleSentence: String,
    val exampleTranslation: String
)

data class QuizQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class PronunciationExercise(
    val id: String,
    val phrase: String,
    val phonetic: String,
    val translation: String,
    val level: String, // A1, A2, B1, B2
    val category: String, // Frases Útiles, Fonemas 'TH', 'R' y 'L', Ritmo & Entonación, Trabalenguas
    val pronunciationTip: String
)

data class WordEvaluation(
    val word: String,
    val isCorrect: Boolean,
    val scorePercentage: Int, // 0 to 100
    val feedback: String = ""
)

data class PronunciationResult(
    val overallScore: Int,
    val recognizedText: String,
    val targetPhrase: String,
    val wordEvaluations: List<WordEvaluation>,
    val feedbackSummary: String
)
