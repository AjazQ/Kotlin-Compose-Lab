package com.ajaz.kotlinlab.ui.demos

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ajaz.kotlinlab.ui.theme.Amber500
import com.ajaz.kotlinlab.ui.theme.MotionTokens
import com.ajaz.kotlinlab.ui.theme.Teal500
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.system.measureTimeMillis

@Composable
fun StructuredConcurrencyDemo() {
    val logs = remember { mutableStateListOf<String>() }
    val scope = rememberCoroutineScope()
    var running by remember { mutableStateOf(false) }
    var job by remember { mutableStateOf<kotlinx.coroutines.Job?>(null) }

    fun log(msg: String) {
        logs.add(0, msg)
        if (logs.size > 10) logs.removeLast()
    }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                enabled = !running,
                onClick = {
                    job = scope.launch {
                        running = true
                        val sequential = measureTimeMillis {
                            delay(400); delay(400); delay(400)
                        }
                        log("Sequential ~${sequential}ms")
                        val parallel = measureTimeMillis {
                            coroutineScope {
                                val a = async { delay(400); "A" }
                                val b = async { delay(400); "B" }
                                val c = async { delay(400); "C" }
                                listOf(a, b, c).awaitAll()
                            }
                        }
                        log("Parallel async/await ~${parallel}ms")
                        running = false
                    }
                }
            ) { Text("Run parallel vs sequential") }
            Button(
                enabled = running,
                onClick = {
                    job?.cancel()
                    running = false
                    log("Cancelled parent — children stop")
                }
            ) { Text("Cancel") }
        }
        if (running) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        logs.forEach { DemoLog(it) }
    }
}

@Composable
fun PerformanceDemo() {
    var slider by remember { mutableFloatStateOf(0f) }
    var rawReads by remember { mutableIntStateOf(0) }
    val parity by remember {
        derivedStateOf {
            if (slider.roundToInt() % 2 == 0) "even" else "odd"
        }
    }
    // Count compositions that read slider directly in this block
    rawReads++

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Slider: ${"%.0f".format(slider)}")
        Slider(value = slider, onValueChange = { slider = it }, valueRange = 0f..100f)
        Text("Recompositions reading slider state: $rawReads")
        Text("derivedStateOf parity: $parity")
        DemoLog("derivedStateOf only invalidates when parity actually changes.")
    }
}

@Composable
fun CustomDrawingDemo() {
    var progress by remember { mutableFloatStateOf(0.65f) }
    var showGrid by remember { mutableStateOf(true) }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            if (showGrid) {
                val step = size.minDimension / 8f
                var x = 0f
                while (x < size.width) {
                    drawLine(Color.Gray.copy(alpha = 0.25f), Offset(x, 0f), Offset(x, size.height))
                    x += step
                }
            }
            val stroke = Stroke(width = 18f, cap = StrokeCap.Round)
            drawArc(
                color = Color.Gray.copy(alpha = 0.35f),
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                style = stroke
            )
            drawArc(
                color = Teal500,
                startAngle = 135f,
                sweepAngle = 270f * progress,
                useCenter = false,
                style = stroke
            )
            drawCircle(Amber500, radius = 10f, center = center)
        }
        Text("Progress: ${(progress * 100).roundToInt()}%")
        Slider(value = progress, onValueChange = { progress = it })
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Grid")
            Spacer(Modifier.size(8.dp))
            Switch(checked = showGrid, onCheckedChange = { showGrid = it })
        }
    }
}

@Composable
fun GesturesDemo() {
    var offset by remember { mutableStateOf(Offset(80f, 80f)) }
    var status by remember { mutableStateOf("Drag the orb · long-press to reset") }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        offset = Offset(80f, 80f)
                        status = "Reset via long-press"
                    }
                )
            }
    ) {
        Box(
            modifier = Modifier
                .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
                .size(56.dp)
                .clip(CircleShape)
                .background(Teal500)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offset += dragAmount
                        status = "Dragging x=${offset.x.roundToInt()} y=${offset.y.roundToInt()}"
                    }
                }
        )
        Text(status, modifier = Modifier.align(Alignment.BottomStart).padding(12.dp))
    }
}

sealed interface SampleUiState {
    data object Loading : SampleUiState
    data class Success(val payload: String) : SampleUiState
    data class Error(val reason: String) : SampleUiState
}

class UiStateViewModel : ViewModel() {
    private val _state = MutableStateFlow<SampleUiState>(SampleUiState.Success("Tap Load or Fail"))
    val state: StateFlow<SampleUiState> = _state

    fun load(succeed: Boolean) {
        viewModelScope.launch {
            _state.value = SampleUiState.Loading
            delay(800)
            _state.value = if (succeed) {
                SampleUiState.Success("Loaded item #${Random.nextInt(10, 99)}")
            } else {
                SampleUiState.Error("Network unavailable (simulated)")
            }
        }
    }
}

@Composable
fun UiStatePatternDemo(vm: UiStateViewModel = viewModel()) {
    val state by vm.state.collectAsStateWithLifecycle()
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        when (val s = state) {
            SampleUiState.Loading -> {
                CircularProgressIndicator()
                Text("Loading…")
            }
            is SampleUiState.Success -> Text(s.payload, style = MaterialTheme.typography.headlineSmall)
            is SampleUiState.Error -> Text("Error: ${s.reason}", color = MaterialTheme.colorScheme.error)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { vm.load(true) }) { Text("Load") }
            Button(onClick = { vm.load(false) }) { Text("Fail") }
        }
        DemoLog("Exhaustive when(state) keeps Loading/Success/Error honest.")
    }
}

private enum class AdvPage { One, Two, Three }

@Composable
fun AdvancedAnimationDemo() {
    var page by remember { mutableStateOf(AdvPage.One) }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AdvPage.entries.forEach { p ->
                Button(onClick = { page = p }) { Text(p.name) }
            }
        }
        AnimatedContent(
            targetState = page,
            transitionSpec = {
                val forward = targetState.ordinal > initialState.ordinal
                if (forward) {
                    (slideInHorizontally { it } + fadeIn(MotionTokens.mediumTween())) togetherWith
                        (slideOutHorizontally { -it } + fadeOut(MotionTokens.shortTween()))
                } else {
                    (slideInHorizontally { -it } + fadeIn(MotionTokens.mediumTween())) togetherWith
                        (slideOutHorizontally { it } + fadeOut(MotionTokens.shortTween()))
                }.using(SizeTransform(clip = false))
            },
            label = "advPages"
        ) { target ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        when (target) {
                            AdvPage.One -> Teal500.copy(alpha = 0.35f)
                            AdvPage.Two -> Amber500.copy(alpha = 0.35f)
                            AdvPage.Three -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.35f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("Page ${target.name}", style = MaterialTheme.typography.headlineSmall)
            }
        }
        DemoLog("AnimatedContent picks directional slide based on state order.")
    }
}
