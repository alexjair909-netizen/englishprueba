package com.example.audio

import com.example.data.model.PronunciationResult
import com.example.data.model.WordEvaluation
import kotlin.math.max
import kotlin.math.min

object PronunciationEvaluator {

    fun evaluate(targetPhrase: String, spokenText: String): PronunciationResult {
        val cleanTarget = cleanSentence(targetPhrase)
        val cleanSpoken = cleanSentence(spokenText)

        val targetWords = cleanTarget.split(" ").filter { it.isNotBlank() }
        val spokenWords = cleanSpoken.split(" ").filter { it.isNotBlank() }

        if (targetWords.isEmpty()) {
            return PronunciationResult(
                overallScore = 0,
                recognizedText = spokenText,
                targetPhrase = targetPhrase,
                wordEvaluations = emptyList(),
                feedbackSummary = "No se detectó texto objetivo."
            )
        }

        if (spokenWords.isEmpty()) {
            val emptyEvaluations = targetWords.map {
                WordEvaluation(word = it, isCorrect = false, scorePercentage = 0, feedback = "No se detectó audio")
            }
            return PronunciationResult(
                overallScore = 0,
                recognizedText = "Audio no detectado",
                targetPhrase = targetPhrase,
                wordEvaluations = emptyEvaluations,
                feedbackSummary = "No se detectó voz clara. Intenta hablar más cerca del micrófono."
            )
        }

        val wordEvaluations = mutableListOf<WordEvaluation>()
        var totalScoreSum = 0

        var spokenIndex = 0
        for (targetWord in targetWords) {
            // Find best match in remaining spoken words within a sliding window of 2 words
            var bestSimilarity = 0.0
            var matchedSpokenIndex = -1

            val searchEnd = min(spokenIndex + 3, spokenWords.size)
            for (i in spokenIndex until searchEnd) {
                val sim = calculateWordSimilarity(targetWord, spokenWords[i])
                if (sim > bestSimilarity) {
                    bestSimilarity = sim
                    matchedSpokenIndex = i
                }
            }

            if (matchedSpokenIndex != -1 && bestSimilarity >= 0.5) {
                spokenIndex = matchedSpokenIndex + 1
            }

            val scorePercent = (bestSimilarity * 100).toInt().coerceIn(0, 100)
            totalScoreSum += scorePercent

            val isCorrect = scorePercent >= 75
            val feedback = when {
                scorePercent >= 90 -> "¡Perfecto!"
                scorePercent >= 75 -> "Buena entonación"
                scorePercent >= 50 -> "Casi correcto"
                else -> "Practica más este fonema"
            }

            wordEvaluations.add(
                WordEvaluation(
                    word = targetWord,
                    isCorrect = isCorrect,
                    scorePercentage = scorePercent,
                    feedback = feedback
                )
            )
        }

        val overallScore = (totalScoreSum / targetWords.size).coerceIn(0, 100)

        val feedbackSummary = when {
            overallScore >= 90 -> "¡Extraordinario! Tu pronunciación y cadencia son casi nativas."
            overallScore >= 80 -> "¡Muy bien! Pronunciación clara y comprensible. Buen ritmo."
            overallScore >= 65 -> "Buen intento. Ajusta la pronunciación de las palabras marcadas en amarillo."
            overallScore >= 45 -> "Sigue practicando. Escucha el audio de referencia a velocidad 0.75x y repite."
            else -> "Intenta hablar más despacio y vocalizar cada sílaba con claridad."
        }

        return PronunciationResult(
            overallScore = overallScore,
            recognizedText = spokenText,
            targetPhrase = targetPhrase,
            wordEvaluations = wordEvaluations,
            feedbackSummary = feedbackSummary
        )
    }

    private fun cleanSentence(input: String): String {
        return input.lowercase()
            .replace(Regex("[^a-z0-9\\s']"), " ")
            .replace(Regex("\\s+"), " ")
            .trim()
    }

    private fun calculateWordSimilarity(word1: String, word2: String): Double {
        if (word1 == word2) return 1.0

        // Handle common phonetic equivalence / numbers
        if (word1 == "7" && word2 == "seven" || word1 == "seven" && word2 == "7") return 1.0
        if (word1 == "3" && word2 == "three" || word1 == "three" && word2 == "3") return 1.0

        val distance = levenshteinDistance(word1, word2)
        val maxLength = max(word1.length, word2.length)
        if (maxLength == 0) return 1.0

        return (1.0 - (distance.toDouble() / maxLength)).coerceIn(0.0, 1.0)
    }

    private fun levenshteinDistance(s1: String, s2: String): Int {
        val dp = Array(s1.length + 1) { IntArray(s2.length + 1) }

        for (i in 0..s1.length) dp[i][0] = i
        for (j in 0..s2.length) dp[0][j] = j

        for (i in 1..s1.length) {
            for (j in 1..s2.length) {
                val cost = if (s1[i - 1] == s2[j - 1]) 0 else 1
                dp[i][j] = min(
                    min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                    dp[i - 1][j - 1] + cost
                )
            }
        }
        return dp[s1.length][s2.length]
    }
}
