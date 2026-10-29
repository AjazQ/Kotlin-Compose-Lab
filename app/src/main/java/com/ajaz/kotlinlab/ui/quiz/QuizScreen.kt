package com.ajaz.kotlinlab.ui.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ajaz.kotlinlab.data.QuizCatalog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(onBack: () -> Unit) {
    val questions = QuizCatalog.questions
    var index by remember { mutableIntStateOf(0) }
    var selected by remember { mutableStateOf<Int?>(null) }
    var score by remember { mutableIntStateOf(0) }
    var finished by remember { mutableStateOf(false) }
    val question = questions.getOrNull(index)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quick Quiz", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (finished || question == null) {
                Text("Quiz complete", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("Score: $score / ${questions.size}", style = MaterialTheme.typography.titleLarge)
                Text(
                    if (score >= questions.size - 1) "Excellent — you’re retaining the lectures."
                    else "Review Language Guide and Routing lectures, then try again.",
                    style = MaterialTheme.typography.bodyLarge
                )
                Button(onClick = {
                    index = 0
                    selected = null
                    score = 0
                    finished = false
                }) { Text("Retry") }
                OutlinedButton(onClick = onBack) { Text("Back to dashboard") }
            } else {
                LinearProgressIndicator(
                    progress = { (index.toFloat()) / questions.size.toFloat() },
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Question ${index + 1} of ${questions.size}", style = MaterialTheme.typography.labelLarge)
                Text(question.prompt, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                question.choices.forEachIndexed { i, choice ->
                    val isSelected = selected == i
                    OutlinedButton(
                        onClick = { if (selected == null) selected = i },
                        enabled = selected == null || isSelected,
                        modifier = Modifier.fillMaxWidth()
                    ) { Text(choice) }
                }
                if (selected != null) {
                    val correct = selected == question.correctIndex
                    Text(
                        if (correct) "Correct" else "Not quite",
                        color = if (correct) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                    Text(question.explanation, style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = {
                            if (selected == question.correctIndex) score++
                            if (index >= questions.lastIndex) {
                                finished = true
                            } else {
                                index++
                                selected = null
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (index >= questions.lastIndex) "See results" else "Next")
                    }
                }
            }
        }
    }
}
