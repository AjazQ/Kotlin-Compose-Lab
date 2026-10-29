package com.ajaz.kotlinlab.data

enum class SkillLevel {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED;

    val displayName: String
        get() = when (this) {
            BEGINNER -> "Beginner"
            INTERMEDIATE -> "Intermediate"
            ADVANCED -> "Advanced"
        }

    val tagline: String
        get() = when (this) {
            BEGINNER -> "Composable building blocks, layouts, and simple state"
            INTERMEDIATE -> "ViewModels, coroutines, navigation, and side effects"
            ADVANCED -> "Concurrency, performance, gestures, and custom drawing"
        }

    companion object {
        fun fromRoute(value: String): SkillLevel =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: BEGINNER
    }
}
