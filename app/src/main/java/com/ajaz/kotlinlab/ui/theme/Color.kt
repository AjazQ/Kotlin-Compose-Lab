package com.ajaz.kotlinlab.ui.theme

import androidx.compose.ui.graphics.Color
import com.ajaz.kotlinlab.data.SkillLevel

val Slate900 = Color(0xFF0F172A)
val Slate800 = Color(0xFF1E293B)
val Slate700 = Color(0xFF334155)
val Slate100 = Color(0xFFF1F5F9)
val Slate50 = Color(0xFFF8FAFC)

val Teal500 = Color(0xFF14B8A6)
val Teal700 = Color(0xFF0F766E)
val Teal100 = Color(0xFFCCFBF1)

val Sky500 = Color(0xFF0EA5E9)
val Sky700 = Color(0xFF0369A1)
val Sky100 = Color(0xFFE0F2FE)

val Amber500 = Color(0xFFF59E0B)
val Amber700 = Color(0xFFB45309)
val Amber100 = Color(0xFFFEF3C7)

val Coral500 = Color(0xFFF97316)

fun SkillLevel.accent(): Color = when (this) {
    SkillLevel.BEGINNER -> Teal500
    SkillLevel.INTERMEDIATE -> Sky500
    SkillLevel.ADVANCED -> Amber500
}

fun SkillLevel.accentDark(): Color = when (this) {
    SkillLevel.BEGINNER -> Teal700
    SkillLevel.INTERMEDIATE -> Sky700
    SkillLevel.ADVANCED -> Amber700
}

fun SkillLevel.accentSoft(): Color = when (this) {
    SkillLevel.BEGINNER -> Teal100
    SkillLevel.INTERMEDIATE -> Sky100
    SkillLevel.ADVANCED -> Amber100
}
