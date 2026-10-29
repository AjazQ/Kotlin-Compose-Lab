package com.ajaz.kotlinlab.data

object ModuleCodeSamples {
    private val samples: Map<String, String> = mapOf(
        "composables_modifiers" to """
@Composable
fun ComposablesModifiersDemo() {
    var count by remember { mutableIntStateOf(0) }
    var highlight by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    if (highlight) Color(0xFF14B8A6).copy(alpha = 0.25f)
                    else MaterialTheme.colorScheme.surfaceVariant
                )
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Favorite, contentDescription = null)
                Spacer(Modifier.width(12.dp))
                Text("Count = ${'$'}count")
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = { count++ }) { Text("Increase") }
            Spacer(Modifier.width(12.dp))
            Text("Highlight")
            Switch(checked = highlight, onCheckedChange = { highlight = it })
        }
    }
}
    """.trimIndent(),
        "layouts" to """
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
                Text("A"); Text("B"); Text("C")
            }
            "Row" -> Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("A"); Text("B"); Text("C")
            }
            else -> Box(Modifier.fillMaxWidth().height(120.dp)) {
                Text("Center", Modifier.align(Alignment.Center))
                Text("TL", Modifier.align(Alignment.TopStart))
                Text("BR", Modifier.align(Alignment.BottomEnd))
            }
        }
    }
}
    """.trimIndent(),
        "basic_state" to """
@Composable
fun BasicStateDemo() {
    // remember keeps state across recompositions
    var value by remember { mutableIntStateOf(0) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Value: ${'$'}value", style = MaterialTheme.typography.headlineMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { value++ }) { Text("+1") }
            Button(onClick = { value -= 1 }) { Text("-1") }
            Button(onClick = { value = 0 }) { Text("Reset") }
        }
    }
}
    """.trimIndent(),
        "user_input" to """
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
        Text("Summary: ${'$'}{name.ifBlank { "(empty)" }}")
        Button(onClick = { /* submit */ }, enabled = canSubmit) {
            Text("Submit")
        }
    }
}
    """.trimIndent(),
        "lazy_lists" to """
@Composable
fun LazyListsDemo() {
    val items = remember { mutableStateListOf(*Array(40) { "Item #${'$'}{it + 1}" }) }
    val listState = rememberLazyListState()
    val visible by remember {
        derivedStateOf {
            val info = listState.layoutInfo
            val first = info.visibleItemsInfo.firstOrNull()?.index ?: 0
            val last = info.visibleItemsInfo.lastOrNull()?.index ?: 0
            first to last
        }
    }

    Column {
        Text("Visible range: ${'$'}{visible.first} .. ${'$'}{visible.second}")
        Button(onClick = { items.add("Item #${'$'}{items.size + 1}") }) {
            Text("Add item")
        }
        LazyColumn(state = listState, modifier = Modifier.height(280.dp)) {
            items(items, key = { it }) { item ->
                Text(item, modifier = Modifier.padding(12.dp))
            }
        }
    }
}
    """.trimIndent(),
        "material_basics" to """
@Composable
fun MaterialBasicsDemo() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Typography", style = MaterialTheme.typography.headlineSmall)
        Text("Title medium", style = MaterialTheme.typography.titleMedium)
        Text(
            "Body uses theme tokens so light/dark stay consistent.",
            style = MaterialTheme.typography.bodyLarge
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                Modifier.size(56.dp)
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
            )
            Box(
                Modifier.size(56.dp)
                    .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(12.dp))
            )
            Box(
                Modifier.size(56.dp)
                    .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(12.dp))
            )
        }
        Card {
            Text(
                "Surface/Card picks tonal color from the active scheme.",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
    """.trimIndent(),
        "state_hoisting" to """
@Composable
fun StateHoistingDemo() {
    // Survives rotation / process recreation
    var saveable by rememberSaveable { mutableIntStateOf(0) }
    // Resets when composition is disposed (e.g. rotation)
    var rememberOnly by remember { mutableIntStateOf(0) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        HoistedCounter("rememberSaveable", saveable) { saveable = it }
        HoistedCounter("remember only", rememberOnly) { rememberOnly = it }
    }
}

@Composable
private fun HoistedCounter(
    label: String,
    value: Int,
    onChange: (Int) -> Unit // state hoisted to parent
) {
    Column(Modifier.padding(12.dp)) {
        Text("${'$'}label: ${'$'}value")
        Row {
            Button(onClick = { onChange(value + 1) }) { Text("+") }
            Button(onClick = { onChange((value - 1).coerceAtLeast(0)) }) { Text("-") }
        }
    }
}
    """.trimIndent(),
        "viewmodel_stateflow" to """
data class CounterUiState(
    val value: Int = 0,
    val loading: Boolean = false,
    val message: String = "Idle"
)

class CounterViewModel : ViewModel() {
    private val _state = MutableStateFlow(CounterUiState())
    val state: StateFlow<CounterUiState> = _state

    fun increment() {
        _state.value = _state.value.copy(
            value = _state.value.value + 1,
            message = "Incremented"
        )
    }

    fun loadAsync() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, message = "Loading…")
            delay(900)
            _state.value = _state.value.copy(
                loading = false,
                value = _state.value.value + 5,
                message = "Loaded +5"
            )
        }
    }
}

@Composable
fun ViewModelStateFlowDemo(vm: CounterViewModel = viewModel()) {
    val ui by vm.state.collectAsStateWithLifecycle()
    Column {
        Text("Count: ${'$'}{ui.value}")
        Text(ui.message)
        if (ui.loading) LinearProgressIndicator(Modifier.fillMaxWidth())
        Button(onClick = vm::increment) { Text("+1") }
        Button(onClick = vm::loadAsync, enabled = !ui.loading) { Text("Load") }
    }
}
    """.trimIndent(),
        "coroutines_compose" to """
@Composable
fun CoroutinesComposeDemo() {
    var ticks by remember { mutableIntStateOf(0) }
    var key by remember { mutableIntStateOf(0) }
    var running by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Restarts when key or running changes; cancelled on leave
    LaunchedEffect(key, running) {
        if (!running) return@LaunchedEffect
        while (isActive) {
            delay(400)
            ticks++
        }
    }

    Column {
        Text("Ticks: ${'$'}ticks | key: ${'$'}key")
        Button(onClick = { running = true }) { Text("Start effect") }
        Button(onClick = { running = false }) { Text("Stop") }
        Button(onClick = { key++ }) { Text("Bump key") }
        Button(onClick = {
            scope.launch {
                delay(500)
                ticks += 10
            }
        }) { Text("Scope +10") }
    }
}
    """.trimIndent(),
        "dispatchers" to """
@Composable
fun DispatchersDemo() {
    val logs = remember { mutableStateListOf<String>() }
    val scope = rememberCoroutineScope()

    fun add(msg: String) {
        logs.add(0, msg)
    }

    Column {
        Button(onClick = {
            scope.launch(Dispatchers.Main) {
                add("Main: ${'$'}{Thread.currentThread().name}")
            }
        }) { Text("Main") }

        Button(onClick = {
            scope.launch {
                val name = withContext(Dispatchers.Default) {
                    var x = 0
                    repeat(200_000) { x += it }
                    Thread.currentThread().name
                }
                add("Default: ${'$'}name")
            }
        }) { Text("Default") }

        Button(onClick = {
            scope.launch {
                val name = withContext(Dispatchers.IO) {
                    delay(200)
                    Thread.currentThread().name
                }
                add("IO: ${'$'}name")
            }
        }) { Text("IO") }

        logs.forEach { Text(it) }
    }
}
    """.trimIndent(),
        "side_effects" to """
@Composable
fun SideEffectsDemo() {
    var enabled by remember { mutableStateOf(false) }
    var beats by remember { mutableIntStateOf(0) }

    DisposableEffect(enabled) {
        if (!enabled) {
            onDispose { }
        } else {
            val job = CoroutineScope(Dispatchers.Main).launch {
                while (isActive) {
                    delay(500)
                    beats++
                }
            }
            onDispose {
                job.cancel() // cleanup when leaving composition / key changes
            }
        }
    }

    Column {
        Switch(checked = enabled, onCheckedChange = { enabled = it })
        Text("Heartbeats: ${'$'}beats")
    }
}
    """.trimIndent(),
        "dynamic_ui" to """
enum class DynamicType { Text, Switch, Slider }
data class DynamicControl(val id: Int, val type: DynamicType)

@Composable
fun DynamicUiDemo() {
    var nextId by remember { mutableIntStateOf(1) }
    val controls = remember { mutableStateListOf<DynamicControl>() }

    Column {
        Button(onClick = {
            controls.add(DynamicControl(nextId++, DynamicType.Text))
        }) { Text("Add Text") }
        Button(onClick = {
            controls.add(DynamicControl(nextId++, DynamicType.Switch))
        }) { Text("Add Switch") }

        controls.forEach { control ->
            key(control.id) {
                when (control.type) {
                    DynamicType.Text -> {
                        var text by remember { mutableStateOf("Dynamic #${'$'}{control.id}") }
                        OutlinedTextField(
                            value = text,
                            onValueChange = { text = it },
                            label = { Text("Text ${'$'}{control.id}") }
                        )
                        TextButton(onClick = { controls.remove(control) }) {
                            Text("Remove")
                        }
                    }
                    DynamicType.Switch -> {
                        var checked by remember { mutableStateOf(false) }
                        Switch(checked = checked, onCheckedChange = { checked = it })
                        TextButton(onClick = { controls.remove(control) }) {
                            Text("Remove")
                        }
                    }
                    DynamicType.Slider -> {
                        var value by remember { mutableFloatStateOf(0.3f) }
                        Slider(value = value, onValueChange = { value = it })
                        TextButton(onClick = { controls.remove(control) }) {
                            Text("Remove")
                        }
                    }
                }
            }
        }
    }
}
    """.trimIndent(),
        "navigation_args" to """
// NavHost routes
const val Detail = "nav_args_detail/{message}"

composable(
    route = Detail,
    arguments = listOf(navArgument("message") { type = NavType.StringType })
) { entry ->
    val message = entry.arguments?.getString("message").orEmpty()
    NavigationArgsDetailDemo(message) { navController.popBackStack() }
}

@Composable
fun NavigationArgsDemo(navController: NavHostController) {
    var message by rememberSaveable { mutableStateOf("Hello from args") }

    Column {
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Message to pass") }
        )
        Button(onClick = {
            val encoded = Uri.encode(message)
            navController.navigate("nav_args_detail/${'$'}encoded")
        }) { Text("Open detail with argument") }
    }
}

@Composable
fun NavigationArgsDetailDemo(message: String, onBack: () -> Unit) {
    Column {
        Text("Received: ${'$'}message")
        Button(onClick = onBack) { Text("Back") }
    }
}
    """.trimIndent(),
        "basic_animation" to """
@Composable
fun BasicAnimationDemo() {
    var expanded by remember { mutableStateOf(false) }
    var showPanel by remember { mutableStateOf(true) }

    val size by animateDpAsState(if (expanded) 160.dp else 72.dp, label = "size")
    val color by animateColorAsState(
        if (expanded) Color(0xFF14B8A6) else MaterialTheme.colorScheme.secondary,
        label = "color"
    )

    Column {
        Box(
            Modifier
                .size(size)
                .clip(RoundedCornerShape(16.dp))
                .background(color)
        )
        Button(onClick = { expanded = !expanded }) {
            Text(if (expanded) "Shrink" else "Expand")
        }
        Switch(checked = showPanel, onCheckedChange = { showPanel = it })
        AnimatedVisibility(visible = showPanel) {
            Text("AnimatedVisibility enter/exit")
        }
    }
}
    """.trimIndent(),
        "structured_concurrency" to """
@Composable
fun StructuredConcurrencyDemo() {
    val scope = rememberCoroutineScope()
    var running by remember { mutableStateOf(false) }
    var job by remember { mutableStateOf<Job?>(null) }

    Button(
        enabled = !running,
        onClick = {
            job = scope.launch {
                running = true
                val sequential = measureTimeMillis {
                    delay(400); delay(400); delay(400)
                }
                val parallel = measureTimeMillis {
                    coroutineScope {
                        val a = async { delay(400); "A" }
                        val b = async { delay(400); "B" }
                        val c = async { delay(400); "C" }
                        awaitAll(a, b, c)
                    }
                }
                // parallel ~= 400ms, sequential ~= 1200ms
                running = false
            }
        }
    ) { Text("Run parallel vs sequential") }

    Button(enabled = running, onClick = {
        job?.cancel()
        running = false
    }) { Text("Cancel") }
}
    """.trimIndent(),
        "performance" to """
@Composable
fun PerformanceDemo() {
    var slider by remember { mutableFloatStateOf(0f) }

    // Recalculates only when the derived value actually changes
    val parity by remember {
        derivedStateOf {
            if (slider.roundToInt() % 2 == 0) "even" else "odd"
        }
    }

    Column {
        Text("Slider: ${'$'}{"%.0f".format(slider)}")
        Slider(value = slider, onValueChange = { slider = it }, valueRange = 0f..100f)
        Text("derivedStateOf parity: ${'$'}parity")
    }
}
    """.trimIndent(),
        "custom_drawing" to """
@Composable
fun CustomDrawingDemo() {
    var progress by remember { mutableFloatStateOf(0.65f) }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        val stroke = Stroke(width = 18f, cap = StrokeCap.Round)
        drawArc(
            color = Color.Gray.copy(alpha = 0.35f),
            startAngle = 135f,
            sweepAngle = 270f,
            useCenter = false,
            style = stroke
        )
        drawArc(
            color = Color(0xFF14B8A6),
            startAngle = 135f,
            sweepAngle = 270f * progress,
            useCenter = false,
            style = stroke
        )
    }
    Slider(value = progress, onValueChange = { progress = it })
}
    """.trimIndent(),
        "gestures" to """
@Composable
fun GesturesDemo() {
    var offset by remember { mutableStateOf(Offset(80f, 80f)) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { offset = Offset(80f, 80f) }
                )
            }
    ) {
        Box(
            modifier = Modifier
                .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
                .size(56.dp)
                .background(Color(0xFF14B8A6), CircleShape)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offset += dragAmount
                    }
                }
        )
    }
}
    """.trimIndent(),
        "ui_state_pattern" to """
sealed interface SampleUiState {
    data object Loading : SampleUiState
    data class Success(val payload: String) : SampleUiState
    data class Error(val reason: String) : SampleUiState
}

class UiStateViewModel : ViewModel() {
    private val _state = MutableStateFlow<SampleUiState>(
        SampleUiState.Success("Tap Load or Fail")
    )
    val state: StateFlow<SampleUiState> = _state

    fun load(succeed: Boolean) {
        viewModelScope.launch {
            _state.value = SampleUiState.Loading
            delay(800)
            _state.value = if (succeed) {
                SampleUiState.Success("Loaded item")
            } else {
                SampleUiState.Error("Network unavailable")
            }
        }
    }
}

@Composable
fun UiStatePatternDemo(vm: UiStateViewModel = viewModel()) {
    val state by vm.state.collectAsStateWithLifecycle()
    when (val s = state) {
        SampleUiState.Loading -> CircularProgressIndicator()
        is SampleUiState.Success -> Text(s.payload)
        is SampleUiState.Error -> Text("Error: ${'$'}{s.reason}")
    }
    Button(onClick = { vm.load(true) }) { Text("Load") }
    Button(onClick = { vm.load(false) }) { Text("Fail") }
}
    """.trimIndent(),
        "advanced_animation" to """
enum class AdvPage { One, Two, Three }

@Composable
fun AdvancedAnimationDemo() {
    var page by remember { mutableStateOf(AdvPage.One) }

    Row {
        AdvPage.entries.forEach { p ->
            Button(onClick = { page = p }) { Text(p.name) }
        }
    }

    AnimatedContent(
        targetState = page,
        transitionSpec = {
            val forward = targetState.ordinal > initialState.ordinal
            if (forward) {
                (slideInHorizontally { it } + fadeIn()) togetherWith
                    (slideOutHorizontally { -it } + fadeOut())
            } else {
                (slideInHorizontally { -it } + fadeIn()) togetherWith
                    (slideOutHorizontally { it } + fadeOut())
            }.using(SizeTransform(clip = false))
        },
        label = "advPages"
    ) { target ->
        Box(Modifier.fillMaxWidth().height(140.dp), contentAlignment = Alignment.Center) {
            Text("Page ${'$'}{target.name}")
        }
    }
}
    """.trimIndent(),

        "game_basics" to """
@Composable
fun GameBasicsDemo() {
    var running by remember { mutableStateOf(false) }
    var paddleX by remember { mutableFloatStateOf(0.5f) }
    var ballX by remember { mutableFloatStateOf(0.5f) }
    var ballY by remember { mutableFloatStateOf(0.35f) }
    var velX by remember { mutableFloatStateOf(0.28f) }
    var velY by remember { mutableFloatStateOf(0.45f) }

    LaunchedEffect(running) {
        if (!running) return@LaunchedEffect
        var last = 0L
        while (true) {
            withFrameNanos { now ->
                if (last == 0L) { last = now; return@withFrameNanos }
                val dt = (now - last) / 1_000_000_000f
                last = now
                ballX += velX * dt
                ballY += velY * dt
                // Walls
                if (ballX < 0f || ballX > 1f) velX = -velX
                if (ballY < 0f) velY = abs(velY)
                // Paddle bounce when moving down
                if (velY > 0f && ballY >= 0.90f && abs(ballX - paddleX) <= 0.14f) {
                    ballY = 0.90f
                    velY = -abs(velY)
                    velX = ((ballX - paddleX) / 0.14f) * 0.55f
                }
            }
        }
    }

    Canvas(Modifier.fillMaxWidth().height(280.dp).pointerInput(Unit) {
        detectDragGestures(
            onDragStart = { offset ->
                paddleX = (offset.x / size.width).coerceIn(0.14f, 0.86f)
            },
            onDrag = { change, _ ->
                paddleX = (change.position.x / size.width).coerceIn(0.14f, 0.86f)
            }
        )
    }) {
        drawCircle(Color(0xFFF59E0B), 18f, Offset(ballX * size.width, ballY * size.height))
        drawRect(
            Color(0xFF14B8A6),
            topLeft = Offset(paddleX * size.width - 60f, size.height * 0.90f),
            size = Size(120f, 20f)
        )
    }
}
    """.trimIndent(),

        "api_retrofit" to """
interface JsonPlaceholderApi {
    @GET("todos/{id}")
    suspend fun getTodo(@Path("id") id: Int): TodoDto
}

object ApiClient {
    val api: JsonPlaceholderApi = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                })
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(JsonPlaceholderApi::class.java)
}

sealed interface ApiUiState {
    data object Idle : ApiUiState
    data object Loading : ApiUiState
    data class Success(val todo: TodoDto) : ApiUiState
    data class Error(val message: String) : ApiUiState
}

class ApiDemoViewModel : ViewModel() {
    private val _state = MutableStateFlow<ApiUiState>(ApiUiState.Idle)
    val state = _state

    fun fetchTodo(id: Int) {
        viewModelScope.launch {
            _state.value = ApiUiState.Loading
            try {
                _state.value = ApiUiState.Success(ApiClient.api.getTodo(id))
            } catch (t: Throwable) {
                _state.value = ApiUiState.Error(t.message ?: "Network error")
            }
        }
    }
}

@Composable
fun ApiRetrofitDemo(vm: ApiDemoViewModel = viewModel()) {
    val state by vm.state.collectAsStateWithLifecycle()
    Button(onClick = { vm.fetchTodo(1) }) { Text("GET /todos/1") }
    when (val s = state) {
        ApiUiState.Loading -> CircularProgressIndicator()
        is ApiUiState.Success -> Text(s.todo.title)
        is ApiUiState.Error -> Text(s.message)
        else -> Unit
    }
}
    """.trimIndent(),

        "datastore_prefs" to """
private val Context.dataStore by preferencesDataStore(name = "lab_prefs")

class UserPrefsRepository(private val context: Context) {
    private val nameKey = stringPreferencesKey("display_name")

    val displayName: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[nameKey].orEmpty()
    }

    suspend fun setDisplayName(value: String) {
        context.dataStore.edit { prefs ->
            prefs[nameKey] = value
        }
    }
}

@Composable
fun DataStorePrefsDemo() {
    val context = LocalContext.current
    val repo = remember { UserPrefsRepository(context) }
    val savedName by repo.displayName.collectAsStateWithLifecycle(initialValue = "")
    var draft by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    OutlinedTextField(value = draft, onValueChange = { draft = it })
    Button(onClick = {
        scope.launch { repo.setDisplayName(draft) }
    }) { Text("Save") }
    Text("Saved: ${'$'}savedName")
}
    """.trimIndent(),

        "room_storage" to """
@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val text: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY createdAt DESC")
    fun observeNotes(): Flow<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NoteEntity): Long

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteById(id: Long)
}

@Database(entities = [NoteEntity::class], version = 1)
abstract class LabDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}

@Composable
fun RoomStorageDemo() {
    val db = remember { LabDatabase.get(LocalContext.current) }
    val notes by db.noteDao().observeNotes()
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val scope = rememberCoroutineScope()
    var draft by remember { mutableStateOf("") }

    OutlinedTextField(value = draft, onValueChange = { draft = it })
    Button(onClick = {
        scope.launch {
            db.noteDao().insert(NoteEntity(text = draft))
            draft = ""
        }
    }) { Text("Insert") }

    notes.forEach { note ->
        Text(note.text)
        TextButton(onClick = {
            scope.launch { db.noteDao().deleteById(note.id) }
        }) { Text("Delete") }
    }
}
    """.trimIndent()
    )

    fun forId(id: String): String =
        samples[id] ?: "// Sample code is not available for this module yet."
}
