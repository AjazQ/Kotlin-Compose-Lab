package com.ajaz.kotlinlab.ui.demos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.ajaz.kotlinlab.ui.theme.Amber500
import com.ajaz.kotlinlab.ui.theme.Teal500
import kotlin.math.abs
import kotlin.random.Random

private const val PaddleY = 0.90f
private const val PaddleHalfW = 0.14f
private const val BallR = 0.035f
private const val BaseSpeed = 0.45f

/**
 * Mini Breakout-style bounce: drag the paddle, bounce the ball.
 * Teaches a Compose game loop with withFrameNanos + Canvas + gestures.
 */
@Composable
fun GameBasicsDemo() {
    var running by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var paddleX by remember { mutableFloatStateOf(0.5f) }
    var ballX by remember { mutableFloatStateOf(0.5f) }
    var ballY by remember { mutableFloatStateOf(0.35f) }
    var velX by remember { mutableFloatStateOf(0.28f) }
    var velY by remember { mutableFloatStateOf(BaseSpeed) }
    var speedScale by remember { mutableFloatStateOf(1f) }

    fun resetBall(downward: Boolean = true) {
        ballX = 0.5f
        ballY = 0.35f
        val dir = if (Random.nextBoolean()) 1f else -1f
        velX = 0.28f * dir * speedScale
        velY = (if (downward) BaseSpeed else -BaseSpeed) * speedScale
    }

    LaunchedEffect(running, lives) {
        if (!running || lives <= 0) return@LaunchedEffect
        var lastNanos = 0L
        while (true) {
            withFrameNanos { now ->
                if (lastNanos == 0L) {
                    lastNanos = now
                    return@withFrameNanos
                }
                val dt = ((now - lastNanos) / 1_000_000_000f).coerceIn(0f, 0.05f)
                lastNanos = now

                var x = ballX + velX * dt
                var y = ballY + velY * dt
                var vx = velX
                var vy = velY

                // Side walls
                if (x - BallR <= 0f) {
                    x = BallR
                    vx = abs(vx)
                } else if (x + BallR >= 1f) {
                    x = 1f - BallR
                    vx = -abs(vx)
                }

                // Ceiling
                if (y - BallR <= 0f) {
                    y = BallR
                    vy = abs(vy)
                }

                // Paddle bounce (only when moving downward)
                val paddleTop = PaddleY
                val nearPaddle = y + BallR >= paddleTop && y - BallR <= paddleTop + 0.04f
                val overPaddle = abs(x - paddleX) <= PaddleHalfW
                if (vy > 0f && nearPaddle && overPaddle) {
                    y = paddleTop - BallR
                    vy = -abs(vy)
                    // Angle from hit offset — edge hits push sideways harder
                    val hit = ((x - paddleX) / PaddleHalfW).coerceIn(-1f, 1f)
                    vx = (hit * 0.55f * speedScale).let { nudged ->
                        if (abs(nudged) < 0.15f) {
                            (if (vx >= 0f) 0.2f else -0.2f) * speedScale
                        } else {
                            nudged
                        }
                    }
                    score += 1
                    speedScale = (speedScale + 0.04f).coerceAtMost(1.8f)
                    vy = -BaseSpeed * speedScale
                }

                // Missed paddle — ball fell off bottom
                if (y - BallR > 1.02f) {
                    lives -= 1
                    if (lives <= 0) {
                        running = false
                    } else {
                        resetBall()
                        x = ballX
                        y = ballY
                        vx = velX
                        vy = velY
                    }
                }

                ballX = x
                ballY = y
                velX = vx
                velY = vy
            }
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "Drag the paddle to bounce the ball. Walls and ceiling reflect; miss and you lose a life.",
            style = MaterialTheme.typography.bodyMedium
        )
        Text("Score: $score   Lives: $lives   Speed ×${"%.1f".format(speedScale)}")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    if (lives <= 0) {
                        score = 0
                        lives = 3
                        speedScale = 1f
                    }
                    resetBall()
                    running = true
                },
                enabled = !running
            ) { Text(if (lives <= 0) "Restart" else "Start") }
            Button(onClick = { running = false }, enabled = running) { Text("Pause") }
        }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clip(RoundedCornerShape(16.dp))
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            paddleX = (offset.x / size.width)
                                .coerceIn(PaddleHalfW, 1f - PaddleHalfW)
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            paddleX = (change.position.x / size.width)
                                .coerceIn(PaddleHalfW, 1f - PaddleHalfW)
                        }
                    )
                }
        ) {
            drawRect(Color(0xFF0F172A))

            val orb = Offset(ballX * size.width, ballY * size.height)
            drawCircle(
                color = Amber500,
                radius = BallR * size.width,
                center = orb
            )

            val paddleW = size.width * PaddleHalfW * 2f
            val paddleH = 22f
            val paddleLeft = paddleX * size.width - paddleW / 2f
            drawRoundRect(
                color = Teal500,
                topLeft = Offset(paddleLeft, PaddleY * size.height),
                size = Size(paddleW, paddleH),
                cornerRadius = CornerRadius(12f, 12f)
            )
        }

        when {
            lives <= 0 -> DemoLog("Game over — tap Restart.")
            running -> DemoLog("Touch/drag on the playfield to move the paddle. Ball bounces on hit.")
            else -> DemoLog("Press Start — keep the ball in play with the paddle.")
        }
    }
}
