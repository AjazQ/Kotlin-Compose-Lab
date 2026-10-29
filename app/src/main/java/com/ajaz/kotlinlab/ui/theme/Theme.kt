package com.ajaz.kotlinlab.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

val LocalThemeMode = compositionLocalOf { ThemeMode.SYSTEM }

private val LightColors = lightColorScheme(
    primary = Teal700,
    onPrimary = Color.White,
    primaryContainer = Teal100,
    onPrimaryContainer = Teal700,
    secondary = Sky700,
    onSecondary = Color.White,
    secondaryContainer = Sky100,
    onSecondaryContainer = Sky700,
    tertiary = Amber700,
    onTertiary = Color.White,
    tertiaryContainer = Amber100,
    onTertiaryContainer = Amber700,
    background = Slate50,
    onBackground = Slate900,
    surface = Color.White,
    onSurface = Slate900,
    surfaceVariant = Slate100,
    onSurfaceVariant = Slate700,
    outline = Color(0xFF94A3B8)
)

private val DarkColors = darkColorScheme(
    primary = Teal500,
    onPrimary = Slate900,
    primaryContainer = Teal700,
    onPrimaryContainer = Teal100,
    secondary = Sky500,
    onSecondary = Slate900,
    secondaryContainer = Sky700,
    onSecondaryContainer = Sky100,
    tertiary = Amber500,
    onTertiary = Slate900,
    tertiaryContainer = Amber700,
    onTertiaryContainer = Amber100,
    background = Slate900,
    onBackground = Slate50,
    surface = Slate800,
    onSurface = Slate50,
    surfaceVariant = Slate700,
    onSurfaceVariant = Slate100,
    outline = Color(0xFF64748B)
)

@Composable
fun KotlinLabTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val systemDark = isSystemInDarkTheme()
    val darkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> systemDark
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AppTypography,
        content = content
    )
}
