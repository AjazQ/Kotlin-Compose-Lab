package com.ajaz.kotlinlab.ui.demos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ajaz.kotlinlab.data.api.ApiClient
import com.ajaz.kotlinlab.data.api.TodoDto
import com.ajaz.kotlinlab.data.storage.LabDatabase
import com.ajaz.kotlinlab.data.storage.NoteEntity
import com.ajaz.kotlinlab.data.storage.UserPrefsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface ApiUiState {
    data object Idle : ApiUiState
    data object Loading : ApiUiState
    data class Success(val todo: TodoDto) : ApiUiState
    data class Error(val message: String) : ApiUiState
}

class ApiDemoViewModel : ViewModel() {
    private val _state = MutableStateFlow<ApiUiState>(ApiUiState.Idle)
    val state: StateFlow<ApiUiState> = _state

    fun fetchTodo(id: Int) {
        viewModelScope.launch {
            _state.value = ApiUiState.Loading
            try {
                val todo = ApiClient.api.getTodo(id)
                _state.value = ApiUiState.Success(todo)
            } catch (t: Throwable) {
                _state.value = ApiUiState.Error(t.message ?: "Unknown network error")
            }
        }
    }
}

@Composable
fun ApiRetrofitDemo(vm: ApiDemoViewModel = viewModel()) {
    val state by vm.state.collectAsStateWithLifecycle()
    var todoId by remember { mutableIntStateOf(1) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "Calls https://jsonplaceholder.typicode.com with Retrofit + coroutines.",
            style = MaterialTheme.typography.bodyMedium
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { todoId = (todoId - 1).coerceAtLeast(1) }) { Text("-") }
            Text("Todo id: $todoId")
            Button(onClick = { todoId += 1 }) { Text("+") }
        }
        Button(
            onClick = { vm.fetchTodo(todoId) },
            enabled = state !is ApiUiState.Loading
        ) { Text("GET /todos/$todoId") }

        when (val s = state) {
            ApiUiState.Idle -> DemoLog("Tap GET to run a real HTTPS request.")
            ApiUiState.Loading -> {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                Text("Loading…")
            }
            is ApiUiState.Success -> {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("id=${s.todo.id}  userId=${s.todo.userId}")
                        Text(s.todo.title, style = MaterialTheme.typography.titleMedium)
                        Text("completed=${s.todo.completed}")
                    }
                }
            }
            is ApiUiState.Error -> Text(
                "Error: ${s.message}",
                color = MaterialTheme.colorScheme.error
            )
        }
        DemoLog("Watch Logcat for OkHttp BASIC logs of the request.")
    }
}

class DataStoreDemoViewModel(private val repo: UserPrefsRepository) : ViewModel() {
    val name = repo.displayName.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")
    val darkHint = repo.preferDarkHint.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    fun saveName(value: String) {
        viewModelScope.launch { repo.setDisplayName(value) }
    }

    fun saveDarkHint(value: Boolean) {
        viewModelScope.launch { repo.setPreferDarkHint(value) }
    }

    companion object {
        fun factory(repo: UserPrefsRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DataStoreDemoViewModel(repo) as T
            }
        }
    }
}

@Composable
fun DataStorePrefsDemo() {
    val context = LocalContext.current
    val repo = remember { UserPrefsRepository(context) }
    val vm: DataStoreDemoViewModel = viewModel(factory = DataStoreDemoViewModel.factory(repo))
    val savedName by vm.name.collectAsStateWithLifecycle()
    val darkHint by vm.darkHint.collectAsStateWithLifecycle()
    var draft by remember(savedName) { mutableStateOf(savedName) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "DataStore writes survive process death. Values below are Flows from preferences.",
            style = MaterialTheme.typography.bodyMedium
        )
        OutlinedTextField(
            value = draft,
            onValueChange = { draft = it },
            label = { Text("Display name") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = { vm.saveName(draft.trim()) }) { Text("Save name") }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Prefer dark hint")
            Spacer(Modifier.weight(1f))
            Switch(checked = darkHint, onCheckedChange = vm::saveDarkHint)
        }
        DemoLog("Saved name (from DataStore Flow): \"${savedName.ifBlank { "(empty)" }}\"")
        DemoLog("Kill and reopen the app — values remain.")
    }
}

class RoomDemoViewModel(private val db: LabDatabase) : ViewModel() {
    val notes = db.noteDao().observeNotes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun add(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            db.noteDao().insert(NoteEntity(text = text.trim()))
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch { db.noteDao().deleteById(id) }
    }

    fun clear() {
        viewModelScope.launch { db.noteDao().clearAll() }
    }

    companion object {
        fun factory(db: LabDatabase) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return RoomDemoViewModel(db) as T
            }
        }
    }
}

@Composable
fun RoomStorageDemo() {
    val context = LocalContext.current
    val db = remember { LabDatabase.get(context) }
    val vm: RoomDemoViewModel = viewModel(factory = RoomDemoViewModel.factory(db))
    val notes by vm.notes.collectAsStateWithLifecycle()
    var draft by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "Room persists rows in SQLite. observeNotes() is a Flow that updates the UI.",
            style = MaterialTheme.typography.bodyMedium
        )
        OutlinedTextField(
            value = draft,
            onValueChange = { draft = it },
            label = { Text("New note") },
            modifier = Modifier.fillMaxWidth()
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                vm.add(draft)
                draft = ""
            }) { Text("Insert") }
            TextButton(onClick = { scope.launch { vm.clear() } }) { Text("Clear all") }
        }
        Text("Notes (${notes.size})", style = MaterialTheme.typography.titleSmall)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(notes, key = { it.id }) { note ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(note.text, modifier = Modifier.weight(1f))
                        TextButton(onClick = { vm.delete(note.id) }) { Text("Delete") }
                    }
                }
            }
        }
        if (notes.isEmpty()) DemoLog("No rows yet — insert a note to write to Room.")
    }
}
