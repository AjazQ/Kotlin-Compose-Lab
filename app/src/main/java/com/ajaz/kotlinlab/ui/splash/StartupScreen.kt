package com.ajaz.kotlinlab.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ajaz.kotlinlab.R
import com.ajaz.kotlinlab.ui.theme.Slate900
import com.ajaz.kotlinlab.ui.theme.Teal700
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun StartupScreen(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale = remember { Animatable(0.82f) }
    val alpha = remember { Animatable(0f) }
    val titleAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch {
            alpha.animateTo(1f, tween(450, easing = FastOutSlowInEasing))
        }
        launch {
            scale.animateTo(1f, tween(650, easing = FastOutSlowInEasing))
        }
        launch {
            delay(220)
            titleAlpha.animateTo(1f, tween(500, easing = FastOutSlowInEasing))
        }
        delay(1650)
        onFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Slate900, Teal700.copy(alpha = 0.55f), Slate900)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .scale(scale.value)
                .alpha(alpha.value)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_launcher),
                contentDescription = null,
                modifier = Modifier
                    .size(108.dp)
                    .clip(CircleShape)
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Kotlin Compose Lab",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.alpha(titleAlpha.value)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Learn by building",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.75f),
                modifier = Modifier.alpha(titleAlpha.value)
            )
        }
    }
}
