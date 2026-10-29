package com.ajaz.kotlinlab.ui.theme

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween

object MotionTokens {
    const val Short = 150
    const val Medium = 300
    const val Long = 500
    const val Stagger = 60

    fun <T> shortTween() = tween<T>(durationMillis = Short, easing = FastOutSlowInEasing)
    fun <T> mediumTween() = tween<T>(durationMillis = Medium, easing = FastOutSlowInEasing)
    fun <T> longTween() = tween<T>(durationMillis = Long, easing = FastOutSlowInEasing)
    fun <T> staggerTween(index: Int) = tween<T>(
        durationMillis = Medium,
        delayMillis = index * Stagger,
        easing = FastOutSlowInEasing
    )
}
