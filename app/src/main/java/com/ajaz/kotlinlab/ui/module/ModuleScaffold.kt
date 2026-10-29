package com.ajaz.kotlinlab.ui.module

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ajaz.kotlinlab.data.LearningModule
import com.ajaz.kotlinlab.ui.components.LevelBadge
import com.ajaz.kotlinlab.ui.components.SoftAccentChip
import com.ajaz.kotlinlab.ui.theme.MotionTokens
import com.ajaz.kotlinlab.ui.theme.Slate900
import com.ajaz.kotlinlab.ui.theme.accent
import com.ajaz.kotlinlab.ui.theme.accentSoft
import kotlinx.coroutines.delay

private enum class ModuleTab { Demo, Learn, Code }

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ModuleScaffold(
    module: LearningModule,
    isCompleted: Boolean = false,
    isFavorite: Boolean = false,
    onToggleComplete: () -> Unit = {},
    onToggleFavorite: () -> Unit = {},
    onBack: () -> Unit,
    demoContent: @Composable () -> Unit
) {
    var tab by remember { mutableStateOf(ModuleTab.Demo) }
    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(module.id) {
        entered = false
        delay(20)
        entered = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(module.title, fontWeight = FontWeight.Bold, maxLines = 1)
                        Text(
                            module.shortSummary,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onToggleFavorite) {
                        Icon(
                            if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = onToggleComplete) {
                        Icon(
                            if (isCompleted) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircle,
                            contentDescription = "Mark complete",
                            tint = if (isCompleted) module.level.accent() else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    LevelBadge(level = module.level, modifier = Modifier.padding(end = 8.dp))
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                ModuleTab.entries.forEachIndexed { index, item ->
                    SegmentedButton(
                        selected = tab == item,
                        onClick = { tab = item },
                        shape = SegmentedButtonDefaults.itemShape(index, ModuleTab.entries.size)
                    ) {
                        Text(item.name)
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            AnimatedContent(
                targetState = tab,
                transitionSpec = {
                    (fadeIn(MotionTokens.mediumTween()) + slideInVertically { it / 10 }) togetherWith
                        fadeOut(MotionTokens.shortTween())
                },
                label = "moduleTab",
                modifier = Modifier.fillMaxSize()
            ) { current ->
                when (current) {
                    ModuleTab.Demo -> {
                        AnimatedVisibility(
                            visible = entered,
                            enter = fadeIn(MotionTokens.mediumTween()) + slideInVertically { it / 8 }
                        ) {
                            Card(
                                modifier = Modifier.fillMaxSize(),
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .verticalScroll(rememberScrollState())
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        "Live demo",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(Modifier.height(12.dp))
                                    demoContent()
                                }
                            }
                        }
                    }
                    ModuleTab.Learn -> LearnPanel(module)
                    ModuleTab.Code -> CodePanel(module)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LearnPanel(module: LearningModule) {
    var stepsVisible by remember { mutableIntStateOf(0) }
    LaunchedEffect(module.id) {
        stepsVisible = 0
        module.steps.indices.forEach { i ->
            delay(70)
            stepsVisible = i + 1
        }
    }

    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("What this teaches", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(module.overview, style = MaterialTheme.typography.bodyLarge)

            Text("Process", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            module.steps.forEachIndexed { index, step ->
                AnimatedVisibility(
                    visible = index < stepsVisible,
                    enter = fadeIn(MotionTokens.mediumTween()) + slideInVertically { it / 6 }
                ) {
                    Text(
                        text = "${index + 1}. $step",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }

            Text("Key APIs", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                module.keyApis.forEach { api ->
                    SoftAccentChip(
                        text = api,
                        accent = module.level.accent(),
                        soft = module.level.accentSoft()
                    )
                }
            }

            Text("Try this", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            module.tryThis.forEachIndexed { index, tip ->
                Text("${index + 1}. $tip", style = MaterialTheme.typography.bodyMedium)
            }

            Text(
                "Open the Code tab to copy a ready-to-use Kotlin sample for this demo.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun CodePanel(module: LearningModule) {
    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current
    val verticalScroll = rememberScrollState()
    val horizontalScroll = rememberScrollState()

    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Sample code",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Copy this into your project to recreate the demo.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                TextButton(
                    onClick = {
                        clipboard.setText(AnnotatedString(module.sampleCode))
                        Toast.makeText(context, "Code copied", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Icon(Icons.Outlined.ContentCopy, contentDescription = null)
                    Spacer(Modifier.padding(horizontal = 4.dp))
                    Text("Copy")
                }
            }

            Spacer(Modifier.height(10.dp))
            Text(
                module.overview,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Slate900)
                    .verticalScroll(verticalScroll)
                    .horizontalScroll(horizontalScroll)
                    .padding(14.dp)
            ) {
                Text(
                    text = module.sampleCode,
                    color = Color(0xFFE2E8F0),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
