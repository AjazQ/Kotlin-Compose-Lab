package com.ajaz.kotlinlab.ui.demos

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ajaz.kotlinlab.ui.theme.Teal500

@Composable
fun ComposablesModifiersDemo() {
    var count by remember { mutableIntStateOf(0) }
    var highlight by remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Composable widgets + Modifier styling", style = MaterialTheme.typography.titleSmall)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(if (highlight) Teal500.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surfaceVariant)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(Icons.Outlined.Favorite, contentDescription = null, tint = Teal500)
                Text("Count = $count")
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = { count++ }) { Text("Increase") }
            Text("Highlight")
            Switch(checked = highlight, onCheckedChange = { highlight = it })
        }
        DemoLog("Modifier order and state drive what you see.")
    }
}

@Composable
fun LayoutsDemo() {
    var mode by remember { mutableStateOf("Column") }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Column", "Row", "Box").forEach { label ->
                Button(onClick = { mode = label }) { Text(label) }
            }
        }
        when (mode) {
            "Column" -> Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                LayoutChip("A"); LayoutChip("B"); LayoutChip("C")
            }
            "Row" -> Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                LayoutChip("A"); LayoutChip("B"); LayoutChip("C")
            }
            else -> Box(modifier = Modifier.fillMaxWidth().height(120.dp)) {
                LayoutChip("Back", Modifier.align(Alignment.Center))
                LayoutChip("TL", Modifier.align(Alignment.TopStart))
                LayoutChip("BR", Modifier.align(Alignment.BottomEnd))
            }
        }
        DemoLog("Same children, different parent layout composable.")
    }
}

@Composable
private fun LayoutChip(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) { Text(text) }
}

@Composable
fun BasicStateDemo() {
    var value by remember { mutableIntStateOf(0) }
    var recompositions by remember { mutableIntStateOf(0) }
    recompositions++
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Value: $value", style = MaterialTheme.typography.headlineMedium)
        Text("Approximate recompositions in this slot: $recompositions")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { value++ }) { Text("+1") }
            Button(onClick = { value -= 1 }) { Text("-1") }
            Button(onClick = { value = 0 }) { Text("Reset") }
        }
        DemoLog("Reading mutableStateOf in composition subscribes you to updates.")
    }
}

@Composable
fun UserInputDemo() {
    var name by remember { mutableStateOf("") }
    var newsletter by remember { mutableStateOf(true) }
    var terms by remember { mutableStateOf(false) }
    val canSubmit = name.isNotBlank() && terms
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(checked = newsletter, onCheckedChange = { newsletter = it })
            Text("Subscribe to newsletter")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = terms, onCheckedChange = { terms = it })
            Text("Accept terms")
        }
        Text("Summary: ${name.ifBlank { "(empty)" }} | newsletter=$newsletter | terms=$terms")
        Button(onClick = {}, enabled = canSubmit) { Text("Submit") }
    }
}

@Composable
fun LazyListsDemo() {
    val items = remember { mutableStateListOf(*Array(40) { "Item #${it + 1}" }) }
    val listState = rememberLazyListState()
    val visible by remember {
        derivedStateOf {
            val info = listState.layoutInfo
            val first = info.visibleItemsInfo.firstOrNull()?.index ?: 0
            val last = info.visibleItemsInfo.lastOrNull()?.index ?: 0
            first to last
        }
    }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Visible range: ${visible.first} .. ${visible.second} / ${items.size}")
        Button(onClick = { items.add("Item #${items.size + 1}") }) { Text("Add item") }
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(items, key = { it }) { item ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Text(item, modifier = Modifier.padding(12.dp))
                }
            }
        }
        DemoLog("Only visible LazyColumn rows are composed.")
    }
}

@Composable
fun MaterialBasicsDemo() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Typography", style = MaterialTheme.typography.headlineSmall)
        Text("Title medium", style = MaterialTheme.typography.titleMedium)
        Text("Body large — theme tokens adapt to light and dark.", style = MaterialTheme.typography.bodyLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ColorSwatch("Primary", MaterialTheme.colorScheme.primary)
            ColorSwatch("Secondary", MaterialTheme.colorScheme.secondary)
            ColorSwatch("Tertiary", MaterialTheme.colorScheme.tertiary)
        }
        Card {
            Text(
                "Surface/Card uses tonal elevation from the active color scheme.",
                modifier = Modifier.padding(16.dp)
            )
        }
        DemoLog("Toggle Light/Dark on Home to see schemes swap.")
    }
}

@Composable
private fun ColorSwatch(label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color)
        )
        Spacer(Modifier.height(4.dp))
        Text(label, style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
fun DemoLog(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
            .padding(10.dp)
    )
}
