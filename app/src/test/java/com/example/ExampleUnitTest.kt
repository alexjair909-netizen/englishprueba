package com.example

import com.example.audio.PronunciationEvaluator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testExactPronunciationEvaluation() {
        val target = "Nice to meet you, I am learning English."
        val spoken = "Nice to meet you, I am learning English."
        val result = PronunciationEvaluator.evaluate(target, spoken)

        assertEquals(100, result.overallScore)
        assertTrue(result.wordEvaluations.all { it.isCorrect })
    }

    @Test
    fun testPartialPronunciationEvaluation() {
        val target = "Nice to meet you"
        val spoken = "Nice to see you"
        val result = PronunciationEvaluator.evaluate(target, spoken)

        // Accuracy is partial because "meet" was spoken as "see"
        assertTrue(result.overallScore in 60..90)
        assertTrue(result.overallScore < 100)
        assertFalse(result.wordEvaluations.find { it.word == "meet" }?.isCorrect ?: true)
    }
}
