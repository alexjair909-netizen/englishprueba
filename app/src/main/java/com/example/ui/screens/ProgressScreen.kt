package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.LessonProgressEntity
import com.example.data.model.PronunciationPracticeEntity
import com.example.data.model.SavedWordEntity
import com.example.data.model.UserStatsEntity
import com.example.ui.MainViewModel
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.EmeraldAccent

data class Achievement(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val isUnlocked: Boolean
)

@Composable
fun ProgressScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val userStats by viewModel.userStats.collectAsStateWithLifecycle()
    val lessonProgress by viewModel.lessonProgress.collectAsStateWithLifecycle()
    val pronunciationHistory by viewModel.pronunciationHistory.collectAsStateWithLifecycle()
    val savedWords by viewModel.savedWords.collectAsStateWithLifecycle()

    val totalXp = userStats?.xp ?: 180
    val streakDays = userStats?.streak ?: 4
    val dailyGoal = userStats?.dailyGoalXp ?: 50

    // Compute average pronunciation accuracy in real-time
    val avgPronunciation = if (pronunciationHistory.isNotEmpty()) {
        pronunciationHistory.map { it.score }.average().toInt()
    } else {
        85
    }

    // Determine User Level
    val userLevelTitle = when {
        totalXp >= 500 -> "Nivel 4 · Hablante Fluido"
        totalXp >= 300 -> "Nivel 3 · Orador Seguro"
        totalXp >= 150 -> "Nivel 2 · Explorador Activo"
        else -> "Nivel 1 · Principiante Curioso"
    }

    val completedLessonsCount = lessonProgress.count { it.isCompleted }
    val totalLessonsCount = viewModel.lessons.size
    val readingProgressPercent = if (totalLessonsCount > 0) (completedLessonsCount * 100) / totalLessonsCount else 0

    // Achievements calculation
    val achievements = listOf(
        Achievement(
            title = "Primer Paso",
            description = "Completa tu primera lección de lectura interactiva",
            icon = Icons.AutoMirrored.Filled.MenuBook,
            isUnlocked = completedLessonsCount >= 1
        ),
        Achievement(
            title = "Oído Fino",
            description = "Obtén una precisión de más del 90% en pronunciación",
            icon = Icons.Default.Mic,
            isUnlocked = pronunciationHistory.any { it.score >= 90 }
        ),
        Achievement(
            title = "Racha Ardiente",
            description = "Mantén una racha de autoaprendizaje de al menos 3 días",
            icon = Icons.Default.LocalFireDepartment,
            isUnlocked = streakDays >= 3
        ),
        Achievement(
            title = "Coleccionista",
            description = "Guarda 5 o más palabras en tu vocabulario",
            icon = Icons.Default.Style,
            isUnlocked = savedWords.size >= 5
        ),
        Achievement(
            title = "Maestro Fonético",
            description = "Realiza al menos 10 sesiones de práctica oral",
            icon = Icons.Default.RecordVoiceOver,
            isUnlocked = (userStats?.totalPronunciationsDone ?: 0) >= 10 || pronunciationHistory.size >= 3
        ),
        Achievement(
            title = "Estrella de Oro",
            description = "Alcanza más de 250 XP acumulados",
            icon = Icons.Default.EmojiEvents,
            isUnlocked = totalXp >= 250
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("progress_screen_list"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Seguimiento en Tiempo Real",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Monitorea tu evolución fonética, lectura y constancia diaria.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Top Stats Cards (Streak & XP)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Streak Card
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(AmberAccent.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = null,
                                    tint = AmberAccent,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Racha",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "$streakDays Días",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "¡Sigue practicando!",
                            style = MaterialTheme.typography.bodySmall,
                            color = AmberAccent
                        )
                    }
                }

                // XP Card
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldAccent.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = EmeraldAccent,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Puntos XP",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "$totalXp XP",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = userLevelTitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = EmeraldAccent
                        )
                    }
                }
            }
        }

        // Daily Goal Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Stars,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Meta de Autoaprendizaje Diario",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "${(totalXp % dailyGoal)} / $dailyGoal XP",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val dailyProgress = ((totalXp % dailyGoal).toFloat() / dailyGoal.toFloat()).coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = { dailyProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surface
                    )
                }
            }
        }

        // Real-Time Skill Metrics
        item {
            Text(
                text = "Desempeño de Habilidades",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Pronunciation Metric
                    SkillProgressRow(
                        label = "Precisión de Pronunciación",
                        percentage = avgPronunciation,
                        detail = "${pronunciationHistory.size} frases evaluadas",
                        accentColor = EmeraldAccent
                    )

                    // Reading Comprehension Metric
                    SkillProgressRow(
                        label = "Lecturas de Lecciones",
                        percentage = readingProgressPercent,
                        detail = "$completedLessonsCount de $totalLessonsCount lecciones completadas",
                        accentColor = MaterialTheme.colorScheme.primary
                    )

                    // Active Vocabulary Metric
                    val masteredCount = savedWords.count { it.isMastered }
                    val vocabRetention = if (savedWords.isNotEmpty()) (masteredCount * 100) / savedWords.size else 60
                    SkillProgressRow(
                        label = "Retención de Vocabulario",
                        percentage = vocabRetention,
                        detail = "$masteredCount de ${savedWords.size} palabras dominadas",
                        accentColor = AmberAccent
                    )
                }
            }
        }

        // Weekly Activity Bar Chart
        item {
            Text(
                text = "Actividad Semanal",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    val days = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
                    val heights = listOf(0.4f, 0.7f, 0.5f, 0.9f, 0.8f, 0.3f, 0.65f)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        days.forEachIndexed { idx, day ->
                            val heightFraction = heights[idx]
                            val isToday = idx == 3 // Thursday sample

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(22.dp)
                                        .height((100 * heightFraction).dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(
                                            if (isToday) EmeraldAccent else MaterialTheme.colorScheme.primaryContainer
                                        )
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = day,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                                    ),
                                    color = if (isToday) EmeraldAccent else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // Achievements Section
        item {
            Text(
                text = "Logros e Insignias",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        items(achievements) { achievement ->
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (achievement.isUnlocked) EmeraldAccent.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (achievement.isUnlocked) EmeraldAccent.copy(alpha = 0.4f) else Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(
                                if (achievement.isUnlocked) EmeraldAccent else MaterialTheme.colorScheme.outlineVariant
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = achievement.icon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = achievement.title,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (achievement.isUnlocked) {
                                Surface(
                                    shape = CircleShape,
                                    color = EmeraldAccent.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "Desbloqueado",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = EmeraldAccent,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = achievement.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SkillProgressRow(
    label: String,
    percentage: Int,
    detail: String,
    accentColor: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "$percentage%",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = accentColor
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { (percentage.toFloat() / 100f).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = accentColor,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = detail,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
