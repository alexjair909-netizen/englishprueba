package com.example.data.api

import android.util.Log
import com.example.BuildConfig
import com.example.data.model.LessonWord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class GeminiGeneratedLesson(
    val title: String,
    val level: String,
    val topic: String,
    val paragraphs: List<GeminiParagraph>,
    val vocabulary: List<LessonWord>
)

data class GeminiParagraph(
    val english: String,
    val spanish: String
)

class GeminiLessonService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun generateLesson(
        topic: String = "Daily Life in New York",
        level: String = "A2"
    ): Result<GeminiGeneratedLesson> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            // Return fallback pre-structured lesson if key is placeholder
            return@withContext Result.success(getCuratedFallbackLesson(topic, level))
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

            val prompt = """
                You are an expert English language educator. Generate an interactive English reading lesson for self-learners.
                Topic: $topic
                CEFR Level: $level (adjust vocabulary and grammar complexity appropriately).
                
                Respond strictly with a single valid JSON object, without markdown formatting or codeblocks:
                {
                  "title": "A short engaging English title",
                  "level": "$level",
                  "topic": "$topic",
                  "paragraphs": [
                    {
                      "english": "Paragraph 1 in clear English...",
                      "spanish": "Traducción fiel al español del párrafo 1..."
                    },
                    {
                      "english": "Paragraph 2 in clear English...",
                      "spanish": "Traducción fiel al español del párrafo 2..."
                    },
                    {
                      "english": "Paragraph 3 in clear English...",
                      "spanish": "Traducción fiel al español del párrafo 3..."
                    }
                  ],
                  "vocabulary": [
                    {
                      "word": "keyword1",
                      "phonetic": "/kˈiːwɜːd/",
                      "partOfSpeech": "Noun/Verb/Adjective",
                      "translation": "Definición y traducción clara en español",
                      "exampleSentence": "Example sentence in English using the keyword.",
                      "exampleTranslation": "Traducción del ejemplo al español."
                    }
                  ]
                }
            """.trimIndent()

            val requestBodyJson = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", prompt)
                            })
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("responseMimeType", "application/json")
                    put("temperature", 0.7)
                })
            }

            val request = Request.Builder()
                .url(url)
                .post(requestBodyJson.toString().toRequestBody(jsonMediaType))
                .build()

            val response = client.newCall(request).execute()
            val responseString = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                Log.w("GeminiService", "API error: ${response.code} $responseString. Using curated lesson.")
                return@withContext Result.success(getCuratedFallbackLesson(topic, level))
            }

            val rootJson = JSONObject(responseString)
            val candidates = rootJson.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val content = firstCandidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val textContent = parts?.optJSONObject(0)?.optString("text") ?: ""

            val parsedLesson = parseLessonJson(textContent, topic, level)
            Result.success(parsedLesson)
        } catch (e: Exception) {
            Log.e("GeminiService", "Exception calling Gemini API: ${e.message}", e)
            Result.success(getCuratedFallbackLesson(topic, level))
        }
    }

    suspend fun defineWord(
        word: String,
        contextSentence: String = ""
    ): Result<LessonWord> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext Result.success(
                LessonWord(
                    word = word,
                    phonetic = "/${word.lowercase()}/",
                    partOfSpeech = "Palabra",
                    translation = "Definición interactiva de \"$word\"",
                    exampleSentence = if (contextSentence.isNotBlank()) contextSentence else "I like learning the word $word.",
                    exampleTranslation = "Me gusta aprender la palabra $word."
                )
            )
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
            val prompt = """
                Provide the definition of the English word "$word" in context: "$contextSentence".
                Respond strictly with valid JSON without markdown:
                {
                  "word": "$word",
                  "phonetic": "/IPA transcription/",
                  "partOfSpeech": "Noun/Verb/Adjective/Adverb",
                  "translation": "Traducción concisa y clara en español",
                  "exampleSentence": "A short clear sentence showing how to use $word.",
                  "exampleTranslation": "Traducción de la frase de ejemplo al español"
                }
            """.trimIndent()

            val requestBodyJson = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply { put("text", prompt) })
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("responseMimeType", "application/json")
                    put("temperature", 0.3)
                })
            }

            val request = Request.Builder()
                .url(url)
                .post(requestBodyJson.toString().toRequestBody(jsonMediaType))
                .build()

            val response = client.newCall(request).execute()
            val responseString = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                return@withContext Result.success(
                    LessonWord(
                        word = word,
                        phonetic = "/${word.lowercase()}/",
                        partOfSpeech = "Vocabulario",
                        translation = "Definición de \"$word\"",
                        exampleSentence = contextSentence,
                        exampleTranslation = ""
                    )
                )
            }

            val rootJson = JSONObject(responseString)
            val candidates = rootJson.optJSONArray("candidates")
            val textContent = candidates?.optJSONObject(0)
                ?.optJSONObject("content")
                ?.optJSONArray("parts")
                ?.optJSONObject(0)
                ?.optString("text") ?: ""

            val wordJson = JSONObject(textContent)
            val parsedWord = LessonWord(
                word = wordJson.optString("word", word),
                phonetic = wordJson.optString("phonetic", "/${word.lowercase()}/"),
                partOfSpeech = wordJson.optString("partOfSpeech", "Palabra"),
                translation = wordJson.optString("translation", "Significado"),
                exampleSentence = wordJson.optString("exampleSentence", contextSentence),
                exampleTranslation = wordJson.optString("exampleTranslation", "")
            )
            Result.success(parsedWord)
        } catch (e: Exception) {
            Log.e("GeminiService", "Error defining word: ${e.message}")
            Result.success(
                LessonWord(
                    word = word,
                    phonetic = "/${word.lowercase()}/",
                    partOfSpeech = "Palabra",
                    translation = "Definición de \"$word\"",
                    exampleSentence = contextSentence,
                    exampleTranslation = ""
                )
            )
        }
    }

    private fun parseLessonJson(jsonString: String, topic: String, level: String): GeminiGeneratedLesson {
        val json = JSONObject(jsonString)
        val title = json.optString("title", "Interactive Lesson: $topic")
        val lvl = json.optString("level", level)
        val top = json.optString("topic", topic)

        val paragraphs = mutableListOf<GeminiParagraph>()
        val paragraphsArray = json.optJSONArray("paragraphs")
        if (paragraphsArray != null) {
            for (i in 0 until paragraphsArray.length()) {
                val pObj = paragraphsArray.optJSONObject(i)
                if (pObj != null) {
                    paragraphs.add(
                        GeminiParagraph(
                            english = pObj.optString("english", ""),
                            spanish = pObj.optString("spanish", "")
                        )
                    )
                }
            }
        }

        val vocabulary = mutableListOf<LessonWord>()
        val vocabArray = json.optJSONArray("vocabulary")
        if (vocabArray != null) {
            for (i in 0 until vocabArray.length()) {
                val vObj = vocabArray.optJSONObject(i)
                if (vObj != null) {
                    vocabulary.add(
                        LessonWord(
                            word = vObj.optString("word", ""),
                            phonetic = vObj.optString("phonetic", ""),
                            partOfSpeech = vObj.optString("partOfSpeech", "Palabra"),
                            translation = vObj.optString("translation", ""),
                            exampleSentence = vObj.optString("exampleSentence", ""),
                            exampleTranslation = vObj.optString("exampleTranslation", "")
                        )
                    )
                }
            }
        }

        return GeminiGeneratedLesson(
            title = title,
            level = lvl,
            topic = top,
            paragraphs = paragraphs,
            vocabulary = vocabulary
        )
    }

    private fun getCuratedFallbackLesson(topic: String, level: String): GeminiGeneratedLesson {
        return GeminiGeneratedLesson(
            title = "A Journey Through Coffee Culture",
            level = level,
            topic = topic,
            paragraphs = listOf(
                GeminiParagraph(
                    english = "Every morning, millions of people begin their daily journey with a warm cup of coffee. The rich aroma fills the air as golden sunlight streams into bustling neighborhood cafés.",
                    spanish = "Cada mañana, millones de personas comienzan su jornada diaria con una taza de café caliente. El rico aroma llena el aire mientras la luz dorada del sol entra en los concurridos cafés de barrio."
                ),
                GeminiParagraph(
                    english = "Baristas skillfully craft espresso drinks, carefully steaming oat milk into silky textures. For many, this simple ritual represents a peaceful pause before diving into ambitious creative projects.",
                    spanish = "Los baristas preparan hábilmente bebidas a base de espresso, vaporizando con cuidado la leche de avena hasta lograr texturas sedosas. Para muchos, este simple ritual representa una pausa pacífica antes de sumergirse en ambiciosos proyectos creativos."
                ),
                GeminiParagraph(
                    english = "Around wooden tables, friends share laughter and lively conversations. Whether discovering new ideas or reading an inspiring novel, the café serves as a welcoming community hub.",
                    spanish = "Alrededor de mesas de madera, los amigos comparten risas y animadas conversaciones. Ya sea descubriendo nuevas ideas o leyendo una novela inspiradora, la cafetería sirve como un acogedor centro comunitario."
                )
            ),
            vocabulary = listOf(
                LessonWord("bustling", "/ˈbʌs.lɪŋ/", "Adjetivo", "Bullicioso, lleno de vida y actividad", "The downtown market is bustling on Saturdays.", "El mercado del centro está muy bullicioso los sábados."),
                LessonWord("skillfully", "/ˈskɪl.fəl.i/", "Adverbio", "Hábilmente, con gran destreza", "She skillfully solved the puzzle in minutes.", "Ella resolvió hábilmente el acertijo en minutos."),
                LessonWord("silky", "/ˈsɪl.ki/", "Adjetivo", "Sedoso, suave y delicado", "The cappuccino had a wonderfully silky foam.", "El capuchino tenía una espuma maravillosamente sedosa."),
                LessonWord("ritual", "/ˈrɪtʃ.u.əl/", "Sustantivo", "Ritual o hábito diario", "Morning journaling has become her favorite ritual.", "Escribir en su diario por la mañana se ha convertido en su ritual favorito."),
                LessonWord("welcoming", "/ˈwel.kəm.ɪŋ/", "Adjetivo", "Acogedor, hospitalario", "The hosts gave us a very welcoming greeting.", "Los anfitriones nos dieron un saludo muy acogedor.")
            )
        )
    }
}
