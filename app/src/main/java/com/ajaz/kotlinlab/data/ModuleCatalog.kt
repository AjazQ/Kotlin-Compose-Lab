package com.ajaz.kotlinlab.data

object ModuleCatalog {
    val all: List<LearningModule> = listOf(
        LearningModule(
            id = "composables_modifiers",
            title = "Composables & Modifiers",
            level = SkillLevel.BEGINNER,
            shortSummary = "Build UI with @Composable functions and Modifier chains",
            overview = "Compose UIs are functions marked with @Composable. Instead of XML attributes, you chain Modifier calls to size, pad, color, and click elements. Recomposition redraws only what changed.",
            steps = listOf(
                "A @Composable function describes UI, it does not imperatively mutate views.",
                "Text, Button, and Icon are building-block composables.",
                "Modifier.padding / background / clickable decorate the same node.",
                "Order matters: padding before background paints differently than after."
            ),
            keyApis = listOf("@Composable", "Modifier", "Text", "Button", "Icon"),
            tryThis = listOf(
                "Tap Increase to change the counter label.",
                "Toggle the highlight switch to restyle the box via Modifier."
            ),
            iconHint = "widgets"
        ),
        LearningModule(
            id = "layouts",
            title = "Layouts",
            level = SkillLevel.BEGINNER,
            shortSummary = "Arrange children with Column, Row, and Box",
            overview = "Column stacks vertically, Row horizontally, and Box stacks on top of each other. Arrangement and Alignment control spacing and position without ConstraintLayout for most screens.",
            steps = listOf(
                "Column places children top-to-bottom.",
                "Row places children start-to-end.",
                "Box overlays children; Alignment pins them.",
                "weight() shares leftover space in Row/Column."
            ),
            keyApis = listOf("Column", "Row", "Box", "Arrangement", "Alignment", "Modifier.weight"),
            tryThis = listOf(
                "Switch layout mode to see the same children rearrange.",
                "Watch how Alignment changes the Box overlay."
            ),
            iconHint = "view_quilt"
        ),
        LearningModule(
            id = "basic_state",
            title = "Basic State & Recomposition",
            level = SkillLevel.BEGINNER,
            shortSummary = "Drive UI updates with remember and mutableStateOf",
            overview = "When state read by a composable changes, Compose schedules recomposition. remember keeps state across recompositions of the same composition slot; without it, values reset every frame.",
            steps = listOf(
                "mutableStateOf holds observable state.",
                "remember { } stores that state for this composition.",
                "Reading state in composition creates a subscription.",
                "Writing state triggers recomposition of readers."
            ),
            keyApis = listOf("remember", "mutableStateOf", "recomposition"),
            tryThis = listOf(
                "Tap the buttons and watch the UI update immediately.",
                "Notice the recomposition counter increase when state changes."
            ),
            iconHint = "sync"
        ),
        LearningModule(
            id = "user_input",
            title = "User Input",
            level = SkillLevel.BEGINNER,
            shortSummary = "Bind TextField, Switch, and Checkbox to state",
            overview = "Compose input is controlled: the UI shows state you pass in, and callbacks update that state. This unidirectional flow keeps a single source of truth.",
            steps = listOf(
                "TextField value comes from state; onValueChange writes back.",
                "Switch and Checkbox use checked + onCheckedChange.",
                "Derived UI (summary) reads the same state.",
                "Validation can gate a submit button from that state."
            ),
            keyApis = listOf("TextField", "Switch", "Checkbox", "onValueChange"),
            tryThis = listOf(
                "Type a name and toggle options.",
                "Watch the live summary and enable/disable Submit."
            ),
            iconHint = "edit"
        ),
        LearningModule(
            id = "lazy_lists",
            title = "Lazy Lists",
            level = SkillLevel.BEGINNER,
            shortSummary = "Efficient scrolling lists with LazyColumn",
            overview = "LazyColumn only composes visible items (plus a small buffer). That is Compose's lazy loading for lists — great for long datasets without creating every row up front.",
            steps = listOf(
                "LazyColumn is a scrolling viewport.",
                "items() declares the dataset, not all composables at once.",
                "As you scroll, Compose disposes off-screen items and creates new ones.",
                "Keys help Compose reuse the correct item identity."
            ),
            keyApis = listOf("LazyColumn", "items", "key", "LazyListState"),
            tryThis = listOf(
                "Scroll the long list and watch the visible-range label update.",
                "Add items and notice smooth insertion."
            ),
            iconHint = "list"
        ),
        LearningModule(
            id = "material_basics",
            title = "Material 3 Basics",
            level = SkillLevel.BEGINNER,
            shortSummary = "Theme colors, typography, and surfaces",
            overview = "MaterialTheme exposes colorScheme and typography. Prefer theme tokens over hard-coded colors so light and dark themes stay consistent.",
            steps = listOf(
                "MaterialTheme.colorScheme provides semantic colors.",
                "Typography styles hierarchy (display, title, body).",
                "Surface and Card apply elevation and tonal color.",
                "Light/dark schemes swap automatically with theme mode."
            ),
            keyApis = listOf("MaterialTheme", "colorScheme", "typography", "Surface", "Card"),
            tryThis = listOf(
                "Compare primary, secondary, and tertiary samples.",
                "Toggle Light/Dark from the home theme control and reopen this module."
            ),
            iconHint = "palette"
        ),
        LearningModule(
            id = "state_hoisting",
            title = "State Hoisting & Saveable",
            level = SkillLevel.INTERMEDIATE,
            shortSummary = "Lift state up and survive process recreation",
            overview = "State hoisting moves state to a parent so children stay reusable and testable. rememberSaveable stores values in a SavedStateHandle-like bundle so rotation does not wipe them.",
            steps = listOf(
                "Child receives value + onChange (stateless).",
                "Parent owns the source of truth.",
                "rememberSaveable persists across configuration changes.",
                "Compare with remember-only state that resets on rotation."
            ),
            keyApis = listOf("state hoisting", "rememberSaveable", "SavedState"),
            tryThis = listOf(
                "Change both counters, then rotate the device.",
                "Saveable keeps its value; remember-only resets."
            ),
            iconHint = "vertical_align_top"
        ),
        LearningModule(
            id = "viewmodel_stateflow",
            title = "ViewModel + StateFlow",
            level = SkillLevel.INTERMEDIATE,
            shortSummary = "Unidirectional data flow with ViewModel",
            overview = "ViewModel survives rotation. Expose UI state via StateFlow and collect with lifecycle awareness so collectors stop when the UI is not STARTED.",
            steps = listOf(
                "ViewModel holds business/UI state.",
                "MutableStateFlow is updated by intents (user actions).",
                "UI collects with collectAsStateWithLifecycle().",
                "One-way flow: UI → events → ViewModel → state → UI."
            ),
            keyApis = listOf("ViewModel", "StateFlow", "collectAsStateWithLifecycle"),
            tryThis = listOf(
                "Tap Load to simulate async work updating StateFlow.",
                "Rotate the device — ViewModel state remains."
            ),
            iconHint = "account_tree"
        ),
        LearningModule(
            id = "coroutines_compose",
            title = "Coroutines in Compose",
            level = SkillLevel.INTERMEDIATE,
            shortSummary = "LaunchedEffect and rememberCoroutineScope",
            overview = "Side-effectful suspend work belongs in coroutine scopes tied to composition. LaunchedEffect restarts when keys change; rememberCoroutineScope is for event-driven launches (clicks).",
            steps = listOf(
                "LaunchedEffect(key) starts a coroutine when composed.",
                "Changing the key cancels and restarts the effect.",
                "rememberCoroutineScope() launches from callbacks.",
                "Leaving the screen cancels structured work."
            ),
            keyApis = listOf("LaunchedEffect", "rememberCoroutineScope", "delay"),
            tryThis = listOf(
                "Start the ticker and watch ticks accumulate.",
                "Change the key to cancel/restart the effect."
            ),
            iconHint = "bolt"
        ),
        LearningModule(
            id = "dispatchers",
            title = "Multi-threading / Dispatchers",
            level = SkillLevel.INTERMEDIATE,
            shortSummary = "Main, Default, and IO dispatchers",
            overview = "Coroutines move work across threads with Dispatchers. Main updates UI, Default is CPU-bound, IO is blocking I/O. withContext switches without blocking the caller thread.",
            steps = listOf(
                "Launch on Main for UI coordination.",
                "withContext(Dispatchers.Default) for CPU work.",
                "withContext(Dispatchers.IO) for disk/network-style work.",
                "Log thread names so you can see the switch."
            ),
            keyApis = listOf("Dispatchers.Main", "Dispatchers.Default", "Dispatchers.IO", "withContext"),
            tryThis = listOf(
                "Run each dispatcher job and compare thread names in the log.",
                "Notice the UI stays responsive during Default/IO work."
            ),
            iconHint = "hub"
        ),
        LearningModule(
            id = "side_effects",
            title = "Side Effects",
            level = SkillLevel.INTERMEDIATE,
            shortSummary = "LaunchedEffect and DisposableEffect lifecycle",
            overview = "Effects bridge Compose to the outside world. DisposableEffect runs cleanup when leaving composition or when keys change — ideal for listeners and tickers you must stop.",
            steps = listOf(
                "Enter composition → effect starts.",
                "DisposableEffect returns onDispose { }.",
                "Leaving the screen runs cleanup.",
                "Key changes dispose the old effect then start a new one."
            ),
            keyApis = listOf("DisposableEffect", "LaunchedEffect", "onDispose"),
            tryThis = listOf(
                "Enable the effect and watch the heartbeat.",
                "Disable it — onDispose stops the ticker (see log)."
            ),
            iconHint = "leak_add"
        ),
        LearningModule(
            id = "dynamic_ui",
            title = "Dynamic Control Loading",
            level = SkillLevel.INTERMEDIATE,
            shortSummary = "Add and remove composables at runtime",
            overview = "Because UI is a function of state, adding an item to a list automatically composes new controls. Removal disposes them. This is dynamic control loading without ViewStub.",
            steps = listOf(
                "Keep a list of control descriptors in state.",
                "Map the list to composables.",
                "Add appends a descriptor → new control appears.",
                "Remove drops it → composition removes that subtree."
            ),
            keyApis = listOf("mutableStateListOf", "AnimatedVisibility", "key"),
            tryThis = listOf(
                "Add Text, Switch, and Slider controls dynamically.",
                "Remove one and watch it animate out."
            ),
            iconHint = "dashboard_customize"
        ),
        LearningModule(
            id = "navigation_args",
            title = "Navigation with Arguments",
            level = SkillLevel.INTERMEDIATE,
            shortSummary = "Pass data between Compose destinations",
            overview = "Navigation Compose routes can include path/query arguments. The destination reads navBackStackEntry.arguments. Type-safe routes are preferred in production; this demo shows the classic pattern.",
            steps = listOf(
                "Declare a route with an {argument} placeholder.",
                "navigate(\"route/value\") passes data.",
                "Destination reads the argument from the back stack entry.",
                "Back pops and restores the previous destination."
            ),
            keyApis = listOf("NavHost", "navArgument", "navigate", "NavBackStackEntry"),
            tryThis = listOf(
                "Enter a message and open the detail route.",
                "Press back to return with the stack preserved."
            ),
            iconHint = "alt_route"
        ),
        LearningModule(
            id = "basic_animation",
            title = "Basic Animation",
            level = SkillLevel.INTERMEDIATE,
            shortSummary = "animate*AsState and AnimatedVisibility",
            overview = "animate*AsState interpolates toward a target when state changes. AnimatedVisibility runs enter/exit transitions when a boolean flips — ideal for showing panels.",
            steps = listOf(
                "Change a target value (size, color, float).",
                "animate*AsState smoothly approaches it.",
                "AnimatedVisibility composes children only while visible.",
                "Enter/exit specs control fade and slide."
            ),
            keyApis = listOf("animateDpAsState", "animateColorAsState", "AnimatedVisibility"),
            tryThis = listOf(
                "Toggle expand and watch size/color animate.",
                "Show/hide the panel with AnimatedVisibility."
            ),
            iconHint = "animation"
        ),
        LearningModule(
            id = "game_basics",
            title = "Game Basics (Canvas Loop)",
            level = SkillLevel.INTERMEDIATE,
            shortSummary = "Bounce a ball with a paddle using Compose Canvas and a frame loop",
            overview = "Yes — Kotlin can build games on Android. For simple 2D games you can use Compose: withFrameNanos for a game loop, Canvas to draw, and pointerInput for controls. Reflect velocity on walls and the paddle; miss the bottom and lose a life. Larger projects often move to LibGDX or another engine, but the language and coroutines skills transfer.",
            steps = listOf(
                "Keep game state in Compose state (score, position, velocity, lives).",
                "Drive updates with LaunchedEffect + withFrameNanos (delta time).",
                "On collision, flip velocity (bounce) instead of teleporting the ball.",
                "Draw entities each frame in Canvas.",
                "Move the paddle from pointer position; pause with a running flag."
            ),
            keyApis = listOf("withFrameNanos", "Canvas", "pointerInput", "LaunchedEffect", "Offset"),
            tryThis = listOf(
                "Start and drag on the playfield — hit the ball to bounce it upward.",
                "Aim with paddle edges to change the bounce angle.",
                "Miss a few to see lives drop, then Restart."
            ),
            iconHint = "sports_esports"
        ),
        LearningModule(
            id = "api_retrofit",
            title = "API Calls with Retrofit",
            level = SkillLevel.INTERMEDIATE,
            shortSummary = "HTTPS GET with Retrofit, OkHttp, and coroutines",
            overview = "Retrofit turns HTTP endpoints into suspend functions. Combined with ViewModel + sealed UiState you get Loading / Success / Error for real network calls. This demo hits JSONPlaceholder over HTTPS.",
            steps = listOf(
                "Define a Retrofit interface with @GET and suspend.",
                "Build Retrofit with baseUrl + Gson converter + OkHttp client.",
                "Call the API from viewModelScope.launch.",
                "Map results into sealed ApiUiState for the Compose UI."
            ),
            keyApis = listOf("Retrofit", "@GET", "suspend", "OkHttp", "GsonConverterFactory", "ViewModel"),
            tryThis = listOf(
                "Change the todo id and tap GET.",
                "Toggle airplane mode to see the Error state.",
                "Open Logcat and filter OkHttp to see the request."
            ),
            iconHint = "cloud"
        ),
        LearningModule(
            id = "datastore_prefs",
            title = "DataStore Preferences",
            level = SkillLevel.INTERMEDIATE,
            shortSummary = "Type-safe key/value storage that replaces SharedPreferences",
            overview = "DataStore Preferences stores small key/value pairs asynchronously and exposes Flows. Edits are transactional via edit { }. Values survive process death — perfect for user settings.",
            steps = listOf(
                "Create a preferences DataStore with preferencesDataStore.",
                "Define Preferences.Key values (string, boolean, …).",
                "Read with data.map { } as a Flow.",
                "Write with edit { prefs[key] = value } inside a coroutine."
            ),
            keyApis = listOf("DataStore", "preferencesDataStore", "stringPreferencesKey", "edit", "Flow"),
            tryThis = listOf(
                "Save a display name and toggle the switch.",
                "Force-stop the app and reopen — values remain.",
                "Watch the Flow update the UI immediately after save."
            ),
            iconHint = "storage"
        ),
        LearningModule(
            id = "room_storage",
            title = "Room Database",
            level = SkillLevel.INTERMEDIATE,
            shortSummary = "SQLite persistence with Entity, Dao, and Flow",
            overview = "Room is an SQLite abstraction. Annotate entities and DAOs; observe queries as Flow so Compose updates when rows change. Inserts/deletes run on background threads via coroutines (room-ktx).",
            steps = listOf(
                "Define @Entity data classes and a @Dao interface.",
                "Build a @Database singleton with Room.databaseBuilder.",
                "Expose observe queries as Flow<List<T>>.",
                "Call suspend insert/delete from viewModelScope."
            ),
            keyApis = listOf("@Entity", "@Dao", "@Database", "Room", "Flow", "suspend"),
            tryThis = listOf(
                "Insert several notes and scroll the list.",
                "Delete one row — the Flow refreshes the UI.",
                "Clear all, then reopen the app to confirm persistence."
            ),
            iconHint = "database"
        ),
        LearningModule(
            id = "structured_concurrency",
            title = "Concurrent Coroutines",
            level = SkillLevel.ADVANCED,
            shortSummary = "async/await, supervisors, and cancellation",
            overview = "Structured concurrency scopes child jobs. async starts concurrent work you later await. SupervisorScope lets siblings fail independently. Canceling the parent cancels children.",
            steps = listOf(
                "coroutineScope { } waits for all children.",
                "async { } returns Deferred results.",
                "await() joins concurrent work.",
                "Cancel stops unfinished children cooperatively."
            ),
            keyApis = listOf("async", "await", "coroutineScope", "SupervisorJob", "cancel"),
            tryThis = listOf(
                "Run parallel tasks and compare total time vs sequential.",
                "Hit Cancel mid-flight and inspect the log."
            ),
            iconHint = "device_hub"
        ),
        LearningModule(
            id = "performance",
            title = "Recomposition & Performance",
            level = SkillLevel.ADVANCED,
            shortSummary = "derivedStateOf, keys, and stable state",
            overview = "Avoid unnecessary recomposition. derivedStateOf recalculates only when inputs change in a meaningful way. key() tells Compose which subtree identity to reuse. Prefer immutable/stable parameters.",
            steps = listOf(
                "Frequent state updates can recompose large trees.",
                "derivedStateOf caches derived values until inputs change.",
                "key(id) preserves identity across list moves.",
                "Read expensive state only where needed."
            ),
            keyApis = listOf("derivedStateOf", "key", "stability", "recomposition"),
            tryThis = listOf(
                "Move the slider quickly — counter ticks often.",
                "Compare derived even/odd label updates vs raw slider reads."
            ),
            iconHint = "speed"
        ),
        LearningModule(
            id = "custom_drawing",
            title = "Canvas & Custom Drawing",
            level = SkillLevel.ADVANCED,
            shortSummary = "Draw shapes and charts with Canvas",
            overview = "Canvas gives a DrawScope for paths, arcs, and text. Prefer drawing for custom visuals that would be awkward as nested composables.",
            steps = listOf(
                "Canvas provides a DrawScope.",
                "drawCircle / drawLine / drawPath paint primitives.",
                "size and center help responsive drawing.",
                "State changes invalidate and redraw."
            ),
            keyApis = listOf("Canvas", "DrawScope", "drawArc", "drawLine"),
            tryThis = listOf(
                "Adjust the progress slider to redraw the arc chart.",
                "Toggle the grid overlay."
            ),
            iconHint = "brush"
        ),
        LearningModule(
            id = "gestures",
            title = "Gesture Handling",
            level = SkillLevel.ADVANCED,
            shortSummary = "Drag, long-press, and pointer input",
            overview = "Modifier.pointerInput with detectDragGestures / detectTapGestures handles advanced touch. Combine with offset state to move elements.",
            steps = listOf(
                "pointerInput installs a gesture coroutine.",
                "detectDragGestures updates offset while dragging.",
                "detectTapGestures handles tap and long-press.",
                "Consuming gestures prevents parent scroll conflicts carefully."
            ),
            keyApis = listOf("pointerInput", "detectDragGestures", "detectTapGestures"),
            tryThis = listOf(
                "Drag the orb around the pad.",
                "Long-press to reset its position."
            ),
            iconHint = "touch_app"
        ),
        LearningModule(
            id = "ui_state_pattern",
            title = "Sealed UiState Pattern",
            level = SkillLevel.ADVANCED,
            shortSummary = "Model Loading, Success, and Error",
            overview = "A sealed UiState hierarchy makes illegal states unrepresentable. The UI switches on Loading/Success/Error and stays exhaustive when new variants appear.",
            steps = listOf(
                "Define sealed interface/class UiState.",
                "ViewModel emits Loading then Success or Error.",
                "UI uses when (state) without else if exhaustive.",
                "Retry maps back to Loading."
            ),
            keyApis = listOf("sealed interface", "UiState", "when", "StateFlow"),
            tryThis = listOf(
                "Load successfully, then force an error.",
                "Use Retry to recover through Loading again."
            ),
            iconHint = "rule"
        ),
        LearningModule(
            id = "advanced_animation",
            title = "Advanced Animation",
            level = SkillLevel.ADVANCED,
            shortSummary = "AnimatedContent and state-driven transitions",
            overview = "AnimatedContent transitions between composable content keyed by state. Content transforms (slide, fade, size transform) keep motion coherent when switching modes.",
            steps = listOf(
                "Hold a state enum/page index.",
                "AnimatedContent(targetState) swaps children.",
                "transitionSpec defines enter/exit pairs.",
                "SizeTransform animates container size changes."
            ),
            keyApis = listOf("AnimatedContent", "togetherWith", "SizeTransform", "slideInHorizontally"),
            tryThis = listOf(
                "Switch pages and watch directional slides.",
                "Jump between states to see transform specs."
            ),
            iconHint = "auto_awesome"
        )
    )

    fun byLevel(level: SkillLevel): List<LearningModule> =
        all.filter { it.level == level }

    fun byId(id: String): LearningModule? =
        all.find { it.id == id }

    fun countFor(level: SkillLevel): Int = byLevel(level).size
}
