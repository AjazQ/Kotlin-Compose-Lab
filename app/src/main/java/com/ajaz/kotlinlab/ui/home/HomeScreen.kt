package com.ajaz.kotlinlab.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.automirrored.outlined.AltRoute
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ajaz.kotlinlab.data.LanguageCatalog
import com.ajaz.kotlinlab.data.ModuleCatalog
import com.ajaz.kotlinlab.data.RoutingCatalog
import com.ajaz.kotlinlab.data.SkillLevel
import com.ajaz.kotlinlab.data.storage.ProgressSnapshot
import com.ajaz.kotlinlab.ui.components.LanguageGuideCard
import com.ajaz.kotlinlab.ui.components.LevelCard
import com.ajaz.kotlinlab.ui.components.RoutingGuideCard
import com.ajaz.kotlinlab.ui.components.ThemeToggleButton
import com.ajaz.kotlinlab.ui.components.next
import com.ajaz.kotlinlab.ui.theme.Amber500
import com.ajaz.kotlinlab.ui.theme.MotionTokens
import com.ajaz.kotlinlab.ui.theme.Sky500
import com.ajaz.kotlinlab.ui.theme.Slate900
import com.ajaz.kotlinlab.ui.theme.Teal500
import com.ajaz.kotlinlab.ui.theme.ThemeMode
import kotlinx.coroutines.delay
import java.util.Calendar

@Composable
fun HomeScreen(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    progress: ProgressSnapshot,
    onLevelSelected: (SkillLevel) -> Unit,
    onLanguageGuideClick: () -> Unit,
    onRoutingGuideClick: () -> Unit,
    onSearchClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onQuizClick: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(40)
        visible = true
    }
    val levels = SkillLevel.entries
    val greeting = remember { greetingForHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) }
    val overall by animateFloatAsState(
        targetValue = progress.overallFraction,
        animationSpec = MotionTokens.longTween(),
        label = "overallProgress"
    )

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                ModernDashboardHeader(
                    greeting = greeting,
                    themeMode = themeMode,
                    onThemeModeChange = onThemeModeChange,
                    onSearchClick = onSearchClick
                )
            }

            item {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(MotionTokens.mediumTween()) +
                        slideInVertically(MotionTokens.mediumTween()) { it / 5 }
                ) {
                    ProgressOverviewCard(
                        progress = progress,
                        overall = overall,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            }

            item {
                Text(
                    "Quick actions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                )
            }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        listOf(
                            QuickAction("Search", Icons.Outlined.Search, Teal500, onSearchClick),
                            QuickAction("Favorites", Icons.Outlined.FavoriteBorder, Amber500, onFavoritesClick),
                            QuickAction("Quiz", Icons.Outlined.Quiz, Sky500, onQuizClick),
                            QuickAction("Language", Icons.Outlined.MenuBook, Teal500, onLanguageGuideClick),
                            QuickAction("Routing", Icons.AutoMirrored.Outlined.AltRoute, Sky500, onRoutingGuideClick)
                        )
                    ) { action ->
                        QuickActionChip(action)
                    }
                }
            }

            item {
                Text(
                    "Guides",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                )
            }
            item {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(MotionTokens.staggerTween(0)) +
                        slideInVertically(MotionTokens.staggerTween(0)) { it / 3 }
                ) {
                    LanguageGuideCard(
                        topicCount = LanguageCatalog.count,
                        onClick = onLanguageGuideClick,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            }
            item {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(MotionTokens.staggerTween(1)) +
                        slideInVertically(MotionTokens.staggerTween(1)) { it / 3 }
                ) {
                    RoutingGuideCard(
                        topicCount = RoutingCatalog.count,
                        onClick = onRoutingGuideClick,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            }

            item {
                Text(
                    "Practice by level",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                )
            }
            itemsIndexed(levels) { index, level ->
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(MotionTokens.staggerTween(index + 2)) +
                        slideInVertically(MotionTokens.staggerTween(index + 2)) { it / 3 }
                ) {
                    LevelCard(
                        level = level,
                        moduleCount = ModuleCatalog.countFor(level),
                        onClick = { onLevelSelected(level) },
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            }
        }
    }
}

private data class QuickAction(
    val label: String,
    val icon: ImageVector,
    val tint: Color,
    val onClick: () -> Unit
)

@Composable
private fun QuickActionChip(action: QuickAction) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = action.onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp)
            .width(76.dp)
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(action.tint.copy(alpha = 0.16f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(action.icon, contentDescription = action.label, tint = action.tint)
        }
        Spacer(Modifier.height(8.dp))
        Text(action.label, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun ModernDashboardHeader(
    greeting: String,
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    onSearchClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
            .background(
                Brush.linearGradient(
                    listOf(Slate900, Color(0xFF0F766E), Color(0xFF0369A1))
                )
            )
            .padding(horizontal = 20.dp, vertical = 26.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "KOTLIN COMPOSE LAB",
                        style = MaterialTheme.typography.labelLarge,
                        color = Teal500,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        greeting,
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Your modern learning dashboard — guides, modules, and progress in one place.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.82f)
                    )
                }
                ThemeToggleButton(
                    themeMode = themeMode,
                    onCycle = { onThemeModeChange(themeMode.next()) }
                )
            }
            Spacer(Modifier.height(18.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White.copy(alpha = 0.12f))
                    .border(1.dp, Color.White.copy(alpha = 0.18f), RoundedCornerShape(18.dp))
                    .clickable(onClick = onSearchClick)
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.Search, contentDescription = null, tint = Color.White.copy(alpha = 0.9f))
                Spacer(Modifier.width(10.dp))
                Text(
                    "Search modules, language & routing lectures…",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.75f)
                )
            }
        }
    }
}

@Composable
private fun ProgressOverviewCard(
    progress: ProgressSnapshot,
    overall: Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Teal500.copy(alpha = 0.10f),
                            Sky500.copy(alpha = 0.08f),
                            Color.Transparent
                        )
                    )
                )
                .padding(18.dp)
        ) {
            Text("Learning progress", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(
                "${progress.overallDone} of ${progress.overallTotal} items completed",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { overall },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(999.dp)),
                color = Teal500,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ProgressStatPill(
                    label = "Modules",
                    value = "${progress.modulesDone}/${progress.moduleTotal}",
                    accent = Teal500,
                    modifier = Modifier.weight(1f)
                )
                ProgressStatPill(
                    label = "Language",
                    value = "${progress.languageDone}/${progress.languageTotal}",
                    accent = Amber500,
                    modifier = Modifier.weight(1f)
                )
                ProgressStatPill(
                    label = "Routing",
                    value = "${progress.routingDone}/${progress.routingTotal}",
                    accent = Sky500,
                    modifier = Modifier.weight(1f)
                )
            }
            if (progress.favorites.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Text(
                    "${progress.favorites.size} favorites saved",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ProgressStatPill(
    label: String,
    value: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(accent.copy(alpha = 0.12f))
            .border(1.dp, accent.copy(alpha = 0.22f), RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = accent, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(4.dp))
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    }
}

private fun greetingForHour(hour: Int): String = when (hour) {
    in 5..11 -> "Good morning"
    in 12..16 -> "Good afternoon"
    in 17..21 -> "Good evening"
    else -> "Welcome back"
}
