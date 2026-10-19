package com.example.audio

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class SpeechManager(private val context: Context) {

    private var tts: TextToSpeech? = null
    private var isTtsInitialized = false

    private val _isTtsReady = MutableStateFlow(false)
    val isTtsReady: StateFlow<Boolean> = _isTtsReady.asStateFlow()

    private var speechRecognizer: SpeechRecognizer? = null
    private val mainHandler = Handler(Looper.getMainLooper())

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    private val _recognizedText = MutableStateFlow<String?>(null)
    val recognizedText: StateFlow<String?> = _recognizedText.asStateFlow()

    private val _soundRms = MutableStateFlow(0f)
    val soundRms: StateFlow<Float> = _soundRms.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        initTts()
    }

    private fun initTts() {
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale.US)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.w("SpeechManager", "TTS English US not supported directly, fallback default")
                    tts?.language = Locale.ENGLISH
                }
                isTtsInitialized = true
                _isTtsReady.value = true
            } else {
                Log.e("SpeechManager", "Failed to initialize TTS")
                _isTtsReady.value = false
            }
        }
    }

    fun speak(text: String, speed: Float = 1.0f) {
        if (!isTtsInitialized || tts == null) {
            initTts()
            return
        }
        try {
            tts?.setSpeechRate(speed.coerceIn(0.5f, 2.0f))
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "lingua_tts_${System.currentTimeMillis()}")
        } catch (e: Exception) {
            Log.e("SpeechManager", "Error speaking text: ${e.message}")
        }
    }

    fun stopSpeaking() {
        try {
            tts?.stop()
        } catch (e: Exception) {
            Log.e("SpeechManager", "Error stopping TTS: ${e.message}")
        }
    }

    fun startListening(onResult: (String) -> Unit) {
        mainHandler.post {
            try {
                if (!SpeechRecognizer.isRecognitionAvailable(context)) {
                    _errorMessage.value = "Reconocimiento de voz no disponible en este dispositivo."
                    _isListening.value = false
                    return@post
                }

                stopListening()

                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                    setRecognitionListener(object : RecognitionListener {
                        override fun onReadyForSpeech(params: Bundle?) {
                            _isListening.value = true
                            _errorMessage.value = null
                            _recognizedText.value = null
                        }

                        override fun onBeginningOfSpeech() {
                            _isListening.value = true
                        }

                        override fun onRmsChanged(rmsdB: Float) {
                            _soundRms.value = ((rmsdB + 2f) / 12f).coerceIn(0f, 1f)
                        }

                        override fun onBufferReceived(buffer: ByteArray?) {}

                        override fun onEndOfSpeech() {
                            _isListening.value = false
                        }

                        override fun onError(error: Int) {
                            _isListening.value = false
                            val message = when (error) {
                                SpeechRecognizer.ERROR_AUDIO -> "Error de grabación de audio."
                                SpeechRecognizer.ERROR_CLIENT -> "Error del cliente de voz."
                                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Permiso de micrófono no otorgado."
                                SpeechRecognizer.ERROR_NETWORK, SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Error de conexión."
                                SpeechRecognizer.ERROR_NO_MATCH -> "No se detectó ninguna palabra. Intenta de nuevo."
                                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "El reconocedor de voz está ocupado."
                                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Se agotó el tiempo de espera para hablar."
                                else -> "Error al reconocer voz (código $error)."
                            }
                            _errorMessage.value = message
                        }

                        override fun onResults(results: Bundle?) {
                            _isListening.value = false
                            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                            val recognized = matches?.firstOrNull() ?: ""
                            _recognizedText.value = recognized
                            if (recognized.isNotBlank()) {
                                onResult(recognized)
                            } else {
                                _errorMessage.value = "No se pudo interpretar lo hablado. Intenta de nuevo."
                            }
                        }

                        override fun onPartialResults(partialResults: Bundle?) {
                            val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                            matches?.firstOrNull()?.let { _recognizedText.value = it }
                        }

                        override fun onEvent(eventType: Int, params: Bundle?) {}
                    })
                }

                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en-US")
                    putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                    putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
                }

                speechRecognizer?.startListening(intent)
                _isListening.value = true
            } catch (e: Exception) {
                _isListening.value = false
                _errorMessage.value = "Error al iniciar micrófono: ${e.localizedMessage}"
            }
        }
    }

    fun stopListening() {
        mainHandler.post {
            try {
                speechRecognizer?.stopListening()
                speechRecognizer?.cancel()
                speechRecognizer?.destroy()
                speechRecognizer = null
            } catch (e: Exception) {
                Log.e("SpeechManager", "Error stopping speech recognizer: ${e.message}")
            } finally {
                _isListening.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun shutdown() {
        stopSpeaking()
        stopListening()
        try {
            tts?.shutdown()
            tts = null
        } catch (e: Exception) {
            Log.e("SpeechManager", "Error shutting down TTS: ${e.message}")
        }
    }
}
