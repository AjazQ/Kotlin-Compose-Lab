package com.ajaz.kotlinlab.ui.demos

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.ajaz.kotlinlab.ui.theme.MotionTokens
import com.ajaz.kotlinlab.ui.theme.Teal500
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

@Composable
fun StateHoistingDemo() {
    var saveable by rememberSaveable { mutableIntStateOf(0) }
    var rememberOnly by remember { mutableIntStateOf(0) }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        HoistedCounter("rememberSaveable (survives rotation)", saveable) { saveable = it }
        HoistedCounter("remember only (resets on rotation)", rememberOnly) { rememberOnly = it }
        DemoLog("Rotate the device to compare persistence.")
    }
}

@Composable
private fun HoistedCounter(label: String, value: Int, onChange: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(label, style = MaterialTheme.typography.titleSmall)
        Text("Value: $value")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { onChange(value + 1) }) { Text("+") }
            Button(onClick = { onChange((value - 1).coerceAtLeast(0)) }) { Text("-") }
        }
    }
}

data class CounterUiState(val value: Int = 0, val loading: Boolean = false, val message: String = "Idle")

class CounterViewModel : ViewModel() {
    private val _state = kotlinx.coroutines.flow.MutableStateFlow(CounterUiState())
    val state = _state

    fun increment() {
        _state.value = _state.value.copy(value = _state.value.value + 1, message = "Incremented")
    }

    fun loadAsync() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, message = "Loading…")
            delay(900)
            _state.value = _state.value.copy(
                loading = false,
                value = _state.value.value + 5,
                message = "Loaded +5 from fake async work"
            )
        }
    }
}

@Composable
fun ViewModelStateFlowDemo(vm: CounterViewModel = viewModel()) {
    val ui by vm.state.collectAsStateWithLifecycle()
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Count: ${ui.value}", style = MaterialTheme.typography.headlineMedium)
        Text(ui.message)
        if (ui.loading) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = vm::increment) { Text("+1") }
            Button(onClick = vm::loadAsync, enabled = !ui.loading) { Text("Load") }
        }
        DemoLog("StateFlow + collectAsStateWithLifecycle keeps UI lifecycle-aware.")
    }
}

@Composable
fun CoroutinesComposeDemo() {
    var ticks by remember { mutableIntStateOf(0) }
    var key by remember { mutableIntStateOf(0) }
    var running by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }

    LaunchedEffect(key, running) {
        if (!running) return@LaunchedEffect
        while (isActive) {
            delay(400)
            ticks++
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Ticks: $ticks | effect key: $key")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { running = true }) { Text("Start effect") }
            Button(onClick = { running = false }) { Text("Stop") }
            Button(onClick = { key++ }) { Text("Bump key") }
        }
        Button(onClick = {
            job?.cancel()
            job = scope.launch {
                delay(500)
                ticks += 10
            }
        }) { Text("rememberCoroutineScope +10") }
        DemoLog("LaunchedEffect restarts when key changes; scope launches from clicks.")
    }
}

@Composable
fun DispatchersDemo() {
    val logs = remember { mutableStateListOf<String>() }
    val scope = rememberCoroutineScope()
    fun add(msg: String) { logs.add(0, msg); if (logs.size > 8) logs.removeLast() }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                scope.launch(Dispatchers.Main) {
                    add("Main: ${Thread.currentThread().name}")
                }
            }) { Text("Main") }
            Button(onClick = {
                scope.launch {
                    val name = withContext(Dispatchers.Default) {
                        var x = 0
                        repeat(200_000) { x += it }
                        Thread.currentThread().name
                    }
                    add("Default: $name")
                }
            }) { Text("Default") }
            Button(onClick = {
                scope.launch {
                    val name = withContext(Dispatchers.IO) {
                        delay(200)
                        Thread.currentThread().name
                    }
                    add("IO: $name")
                }
            }) { Text("IO") }
        }
        logs.forEach { DemoLog(it) }
        if (logs.isEmpty()) DemoLog("Run a dispatcher to log thread names.")
    }
}

@Composable
fun SideEffectsDemo() {
    var enabled by remember { mutableStateOf(false) }
    var beats by remember { mutableIntStateOf(0) }
    val logs = remember { mutableStateListOf<String>() }

    DisposableEffect(enabled) {
        if (!enabled) {
            onDispose { }
        } else {
            logs.add(0, "Effect started")
            val job = kotlinx.coroutines.CoroutineScope(Dispatchers.Main).launch {
                while (isActive) {
                    delay(500)
                    beats++
                }
            }
            onDispose {
                job.cancel()
                logs.add(0, "onDispose: ticker cancelled")
            }
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Enable effect")
            Spacer(Modifier.width(8.dp))
            Switch(checked = enabled, onCheckedChange = { enabled = it })
        }
        Text("Heartbeats: $beats")
        logs.take(5).forEach { DemoLog(it) }
    }
}

private enum class DynamicType { Text, Switch, Slider }

private data class DynamicControl(val id: Int, val type: DynamicType)

@Composable
fun DynamicUiDemo() {
    var nextId by remember { mutableIntStateOf(1) }
    val controls = remember { mutableStateListOf<DynamicControl>() }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { controls.add(DynamicControl(nextId++, DynamicType.Text)) }) { Text("Add Text") }
            Button(onClick = { controls.add(DynamicControl(nextId++, DynamicType.Switch)) }) { Text("Add Switch") }
            Button(onClick = { controls.add(DynamicControl(nextId++, DynamicType.Slider)) }) { Text("Add Slider") }
        }
        controls.forEach { control ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(MotionTokens.mediumTween()) + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    when (control.type) {
                        DynamicType.Text -> {
                            var text by remember { mutableStateOf("Dynamic #${control.id}") }
                            Row(
                                Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = text,
                                    onValueChange = { text = it },
                                    modifier = Modifier.weight(1f),
                                    label = { Text("Text ${control.id}") }
                                )
                                TextButton(onClick = { controls.remove(control) }) { Text("Remove") }
                            }
                        }
                        DynamicType.Switch -> {
                            var checked by remember { mutableStateOf(false) }
                            Row(
                                Modifier.padding(12.dp).fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Switch ${control.id}")
                                    Spacer(Modifier.width(8.dp))
                                    Switch(checked = checked, onCheckedChange = { checked = it })
                                }
                                TextButton(onClick = { controls.remove(control) }) { Text("Remove") }
                            }
                        }
                        DynamicType.Slider -> {
                            var value by remember { mutableFloatStateOf(0.3f) }
                            Column(Modifier.padding(12.dp)) {
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Slider ${control.id}: ${"%.2f".format(value)}")
                                    TextButton(onClick = { controls.remove(control) }) { Text("Remove") }
                                }
                                Slider(value = value, onValueChange = { value = it })
                            }
                        }
                    }
                }
            }
        }
        if (controls.isEmpty()) DemoLog("Add controls — composition follows the list state.")
    }
}

@Composable
fun NavigationArgsDemo(navController: NavHostController) {
    var message by rememberSaveable { mutableStateOf("Hello from args") }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Message to pass") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = {
            val encoded = android.net.Uri.encode(message)
            navController.navigate("nav_args_detail/$encoded")
        }) { Text("Open detail with argument") }
        DemoLog("Destination reads the path argument from the route.")
    }
}

@Composable
fun NavigationArgsDetailDemo(message: String, onBack: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Received argument", style = MaterialTheme.typography.titleMedium)
        Text(message, style = MaterialTheme.typography.headlineSmall)
        Button(onClick = onBack) { Text("Back") }
        DemoLog("This screen was opened via navigation with a string arg.")
    }
}

@Composable
fun BasicAnimationDemo() {
    var expanded by remember { mutableStateOf(false) }
    var showPanel by remember { mutableStateOf(true) }
    val size by animateDpAsState(if (expanded) 160.dp else 72.dp, label = "size")
    val color by animateColorAsState(
        if (expanded) Teal500 else MaterialTheme.colorScheme.secondary,
        label = "color"
    )
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(RoundedCornerShape(16.dp))
                .background(color)
        )
        Button(onClick = { expanded = !expanded }) { Text(if (expanded) "Shrink" else "Expand") }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Show panel")
            Spacer(Modifier.width(8.dp))
            Switch(checked = showPanel, onCheckedChange = { showPanel = it })
        }
        AnimatedVisibility(visible = showPanel) {
            DemoLog("AnimatedVisibility runs enter/exit transitions.")
        }
    }
}
