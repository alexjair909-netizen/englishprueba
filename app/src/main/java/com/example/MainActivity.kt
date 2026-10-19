package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.MainViewModel
import com.example.ui.NavigationTab
import com.example.ui.components.LinguaBottomNavBar
import com.example.ui.components.LinguaTopBar
import com.example.ui.components.WordLookupBottomSheet
import com.example.ui.screens.LessonsScreen
import com.example.ui.screens.ProgressScreen
import com.example.ui.screens.PronunciationScreen
import com.example.ui.screens.VocabularyScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppRoot()
            }
        }
    }
}

@Composable
fun MainAppRoot(viewModel: MainViewModel = viewModel()) {
    val context = LocalContext.current
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val userStats by viewModel.userStats.collectAsStateWithLifecycle()
    val activeWordDetail by viewModel.activeWordDetail.collectAsStateWithLifecycle()
    val toastMessage by viewModel.toastNotification.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(toastMessage) {
        toastMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    val streakDays = userStats?.streak ?: 4
    val totalXp = userStats?.xp ?: 180

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            LinguaTopBar(
                streakDays = streakDays,
                totalXp = totalXp
            )
        },
        bottomBar = {
            LinguaBottomNavBar(
                currentTab = currentTab,
                onTabSelected = { tab -> viewModel.setTab(tab) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Crossfade(targetState = currentTab, label = "tab_transition") { tab ->
            when (tab) {
                NavigationTab.LESSONS -> LessonsScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
                NavigationTab.PRONUNCIATION -> PronunciationScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
                NavigationTab.VOCABULARY -> VocabularyScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
                NavigationTab.PROGRESS -> ProgressScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }

        // Active Word Detail Bottom Sheet
        activeWordDetail?.let { word ->
            WordLookupBottomSheet(
                word = word,
                onPlayAudio = { viewModel.playWordAudio(it) },
                onSaveWord = { viewModel.saveWordFromLesson(it) },
                onDismiss = { viewModel.dismissWordDetail() }
            )
        }
    }
}
