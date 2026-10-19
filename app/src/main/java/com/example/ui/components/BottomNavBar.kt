package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.RecordVoiceOver
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.NavigationTab

@Composable
fun LinguaBottomNavBar(
    currentTab: NavigationTab,
    onTabSelected: (NavigationTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.testTag("bottom_nav_bar"),
        windowInsets = WindowInsets.navigationBars,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp
    ) {
        // Lecciones
        NavigationBarItem(
            selected = currentTab == NavigationTab.LESSONS,
            onClick = { onTabSelected(NavigationTab.LESSONS) },
            icon = {
                Icon(
                    imageVector = if (currentTab == NavigationTab.LESSONS) Icons.AutoMirrored.Filled.MenuBook else Icons.AutoMirrored.Outlined.MenuBook,
                    contentDescription = stringResource(R.string.nav_lessons)
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.nav_lessons),
                    fontWeight = if (currentTab == NavigationTab.LESSONS) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("nav_item_lessons")
        )

        // Pronunciación
        NavigationBarItem(
            selected = currentTab == NavigationTab.PRONUNCIATION,
            onClick = { onTabSelected(NavigationTab.PRONUNCIATION) },
            icon = {
                Icon(
                    imageVector = if (currentTab == NavigationTab.PRONUNCIATION) Icons.Filled.RecordVoiceOver else Icons.Outlined.RecordVoiceOver,
                    contentDescription = stringResource(R.string.nav_pronunciation)
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.nav_pronunciation),
                    fontWeight = if (currentTab == NavigationTab.PRONUNCIATION) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("nav_item_pronunciation")
        )

        // Vocabulario
        NavigationBarItem(
            selected = currentTab == NavigationTab.VOCABULARY,
            onClick = { onTabSelected(NavigationTab.VOCABULARY) },
            icon = {
                Icon(
                    imageVector = if (currentTab == NavigationTab.VOCABULARY) Icons.Filled.Bookmarks else Icons.Outlined.Bookmarks,
                    contentDescription = stringResource(R.string.nav_vocabulary)
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.nav_vocabulary),
                    fontWeight = if (currentTab == NavigationTab.VOCABULARY) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("nav_item_vocabulary")
        )

        // Progreso
        NavigationBarItem(
            selected = currentTab == NavigationTab.PROGRESS,
            onClick = { onTabSelected(NavigationTab.PROGRESS) },
            icon = {
                Icon(
                    imageVector = if (currentTab == NavigationTab.PROGRESS) Icons.Filled.AutoGraph else Icons.Outlined.AutoGraph,
                    contentDescription = stringResource(R.string.nav_progress)
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.nav_progress),
                    fontWeight = if (currentTab == NavigationTab.PROGRESS) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("nav_item_progress")
        )
    }
}
