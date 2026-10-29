package com.ajaz.kotlinlab.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BrightnessAuto
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ajaz.kotlinlab.ui.theme.ThemeMode

@Composable
fun ThemeToggleButton(
    themeMode: ThemeMode,
    onCycle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = when (themeMode) {
        ThemeMode.SYSTEM -> Icons.Outlined.BrightnessAuto
        ThemeMode.LIGHT -> Icons.Outlined.LightMode
        ThemeMode.DARK -> Icons.Outlined.DarkMode
    }
    val label = when (themeMode) {
        ThemeMode.SYSTEM -> "Theme: System"
        ThemeMode.LIGHT -> "Theme: Light"
        ThemeMode.DARK -> "Theme: Dark"
    }
    IconButton(onClick = onCycle, modifier = modifier) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

fun ThemeMode.next(): ThemeMode = when (this) {
    ThemeMode.SYSTEM -> ThemeMode.LIGHT
    ThemeMode.LIGHT -> ThemeMode.DARK
    ThemeMode.DARK -> ThemeMode.SYSTEM
}
