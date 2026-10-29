package com.ajaz.kotlinlab.ui.dashboard

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ajaz.kotlinlab.data.ModuleCatalog
import com.ajaz.kotlinlab.data.SkillLevel
import com.ajaz.kotlinlab.ui.components.LevelBadge
import com.ajaz.kotlinlab.ui.components.ModuleCard
import com.ajaz.kotlinlab.ui.components.SoftAccentChip
import com.ajaz.kotlinlab.ui.theme.MotionTokens
import com.ajaz.kotlinlab.ui.theme.accent
import com.ajaz.kotlinlab.ui.theme.accentSoft
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    level: SkillLevel,
    onBack: () -> Unit,
    onModuleClick: (String) -> Unit
) {
    val modules = remember(level) { ModuleCatalog.byLevel(level) }
    var visible by remember(level) { mutableStateOf(false) }
    var animatedCount by remember(level) { mutableIntStateOf(0) }

    LaunchedEffect(level) {
        visible = false
        animatedCount = 0
        delay(30)
        visible = true
        modules.indices.forEach { i ->
            delay(40)
            animatedCount = i + 1
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(level.displayName, fontWeight = FontWeight.Bold)
                        Text(
                            text = level.tagline,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    LevelBadge(level = level, modifier = Modifier.padding(end = 12.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        listOf(level.accentSoft().copy(alpha = 0.45f), Color.Transparent)
                    )
                )
        ) {
            AnimatedContent(
                targetState = level,
                transitionSpec = {
                    (fadeIn(MotionTokens.mediumTween()) + slideInVertically { it / 8 }) togetherWith
                        fadeOut(MotionTokens.shortTween())
                },
                label = "dashboardLevel"
            ) { current ->
                val list = ModuleCatalog.byLevel(current)
                LazyColumn(
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            SoftAccentChip(
                                text = "$animatedCount / ${list.size} modules",
                                accent = current.accent(),
                                soft = current.accentSoft()
                            )
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Interactive demos with detailed learning notes",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                    itemsIndexed(list, key = { _, m -> m.id }) { index, module ->
                        AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn(MotionTokens.staggerTween(index)) +
                                slideInVertically(
                                    animationSpec = MotionTokens.staggerTween(index),
                                    initialOffsetY = { it / 4 }
                                )
                        ) {
                            ModuleCard(
                                module = module,
                                onClick = { onModuleClick(module.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}
