package com.ajaz.kotlinlab.data

object RoutingCatalog {
    val all: List<RoutingTopic> = listOf(
        RoutingTopic(
            id = "routes_navhost",
            title = "Routes & NavHost",
            shortSummary = "Declare destinations and host them in a NavHost",
            overview = "In Jetpack Navigation Compose, each screen is a route string (or type-safe route). NavHost is the container that swaps destinations as the back stack changes. One Activity typically owns a single top-level NavHost.",
            keyPoints = listOf(
                "A route identifies a destination (e.g. home, details/{id}).",
                "NavHost(navController, startDestination) registers composable destinations.",
                "Only the destination at the top of the back stack is composed.",
                "Keep route names stable — they are your app's URL-like contract.",
                "startDestination is shown when the graph is first created."
            ),
            sampleCode = """
object Routes {
    const val Home = "home"
    const val Profile = "profile"
}

NavHost(
    navController = navController,
    startDestination = Routes.Home
) {
    composable(Routes.Home) { HomeScreen() }
    composable(Routes.Profile) { ProfileScreen() }
}
""".trimIndent(),
            relatedApis = listOf("NavHost", "composable", "startDestination", "route")
        ),
        RoutingTopic(
            id = "navigate_backstack",
            title = "Navigate & Back Stack",
            shortSummary = "push, pop, and single-top behavior",
            overview = "navigate() pushes a destination onto the back stack. The system Back button (or popBackStack) removes the current destination. Understanding the stack prevents duplicate screens and broken Back behavior.",
            keyPoints = listOf(
                "navController.navigate(route) pushes a new entry.",
                "popBackStack() returns to the previous entry.",
                "launchSingleTop avoids stacking the same route repeatedly.",
                "popUpTo(route) clears entries up to a destination.",
                "inclusive = true also removes the popUpTo destination."
            ),
            sampleCode = """
// Push Details
navController.navigate("details/42")

// Back one screen
navController.popBackStack()

// Navigate Home and clear everything above it
navController.navigate(Routes.Home) {
    popUpTo(Routes.Home) { inclusive = true }
    launchSingleTop = true
}
""".trimIndent(),
            relatedApis = listOf("navigate", "popBackStack", "popUpTo", "launchSingleTop", "NavOptions")
        ),
        RoutingTopic(
            id = "path_arguments",
            title = "Path Arguments",
            shortSummary = "Pass required data inside the route path",
            overview = "Path arguments embed values in the route, similar to /users/42. Declare placeholders with {name}, provide navArgument types, then read them from NavBackStackEntry. Always encode strings that may contain slashes.",
            keyPoints = listOf(
                "Declare route as details/{id}.",
                "Use navArgument(\"id\") { type = NavType.StringType } (or IntType).",
                "Navigate with details/42 or details/${'$'}id.",
                "Read with entry.arguments?.getString(\"id\").",
                "Uri.encode() string args that may include reserved characters."
            ),
            sampleCode = """
const val Details = "details/{id}"

composable(
    route = Details,
    arguments = listOf(navArgument("id") { type = NavType.StringType })
) { entry ->
    val id = entry.arguments?.getString("id").orEmpty()
    DetailsScreen(id = id)
}

// Caller
navController.navigate("details/${'$'}{Uri.encode(userId)}")
""".trimIndent(),
            relatedApis = listOf("navArgument", "NavType", "NavBackStackEntry", "Uri.encode")
        ),
        RoutingTopic(
            id = "optional_query_args",
            title = "Optional & Query Arguments",
            shortSummary = "Defaults, nullable args, and query-style params",
            overview = "Not every parameter belongs in the path. Optional arguments use defaultValue or nullable=true. Query-style routes like search?q={q} are useful for filters that may be absent.",
            keyPoints = listOf(
                "defaultValue supplies a fallback when the arg is missing.",
                "nullable = true allows null for reference types.",
                "Query patterns: search?q={query}.",
                "Keep required IDs in the path; keep filters optional.",
                "Validate args in the destination before using them."
            ),
            sampleCode = """
const val Search = "search?q={query}"

composable(
    route = Search,
    arguments = listOf(
        navArgument("query") {
            type = NavType.StringType
            defaultValue = ""
        }
    )
) { entry ->
    val query = entry.arguments?.getString("query").orEmpty()
    SearchScreen(query)
}

navController.navigate("search?q=coroutines")
""".trimIndent(),
            relatedApis = listOf("defaultValue", "nullable", "query arguments", "NavType.StringType")
        ),
        RoutingTopic(
            id = "nested_graphs",
            title = "Nested Navigation Graphs",
            shortSummary = "Feature graphs, start destinations, and scoped stacks",
            overview = "Large apps split navigation into nested graphs (auth, main, settings). Each graph has its own start destination. Nesting keeps feature routes encapsulated and makes popUpTo clearer across features.",
            keyPoints = listOf(
                "navigation(route = \"main\", startDestination = \"feed\") { ... }",
                "Nested graphs group related screens.",
                "You can navigate to a graph route to enter its start destination.",
                "Use popUpTo the graph route to leave a whole feature.",
                "Pass the same NavController; nesting is structural, not a new controller."
            ),
            sampleCode = """
NavHost(navController, startDestination = "auth") {
    navigation(route = "auth", startDestination = "login") {
        composable("login") { LoginScreen(onOk = {
            navController.navigate("main") {
                popUpTo("auth") { inclusive = true }
            }
        }) }
        composable("register") { RegisterScreen() }
    }
    navigation(route = "main", startDestination = "feed") {
        composable("feed") { FeedScreen() }
        composable("profile") { ProfileScreen() }
    }
}
""".trimIndent(),
            relatedApis = listOf("navigation()", "nested graph", "startDestination", "feature module routing")
        ),
        RoutingTopic(
            id = "bottom_nav_routing",
            title = "Bottom Bar Routing",
            shortSummary = "Multiple tabs and independent back stacks",
            overview = "Bottom navigation usually maps each tab to a top-level route. For polished UX, each tab can keep its own back stack so leaving and returning a tab restores scroll/position. NavigationUI helpers or manual selected-tab state both work.",
            keyPoints = listOf(
                "One selected route mirrors the active tab.",
                "On tab click: navigate(tabRoute) { launchSingleTop=true; restoreState=true; popUpTo(start) { saveState=true } }.",
                "saveState/restoreState preserve each tab's stack.",
                "Avoid stacking the same tab repeatedly (launchSingleTop).",
                "Keep tab roots as sibling destinations in the top graph."
            ),
            sampleCode = """
val tabs = listOf("feed", "search", "settings")

Scaffold(
    bottomBar = {
        NavigationBar {
            tabs.forEach { tab ->
                NavigationBarItem(
                    selected = currentRoute == tab,
                    onClick = {
                        navController.navigate(tab) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(Icons.Default.Home, null) },
                    label = { Text(tab) }
                )
            }
        }
    }
) { /* NavHost */ }
""".trimIndent(),
            relatedApis = listOf("NavigationBar", "saveState", "restoreState", "findStartDestination", "launchSingleTop")
        ),
        RoutingTopic(
            id = "deep_links",
            title = "Deep Links",
            shortSummary = "Open a destination from a URL or notification",
            overview = "Deep links map external URIs onto routes so notifications, app links, and web URLs can open the right screen. Declare navDeepLink patterns on destinations and verify intent filters in the manifest for App Links.",
            keyPoints = listOf(
                "Add deepLinks = listOf(navDeepLink { uriPattern = \"...\" }).",
                "Path placeholders align with route args.",
                "Manifest intent-filters required for https App Links.",
                "Test with adb shell am start -a VIEW -d URL.",
                "Handle missing/invalid args with a safe fallback screen."
            ),
            sampleCode = """
composable(
    route = "details/{id}",
    arguments = listOf(navArgument("id") { type = NavType.StringType }),
    deepLinks = listOf(
        navDeepLink { uriPattern = "https://example.com/details/{id}" },
        navDeepLink { uriPattern = "lab://details/{id}" }
    )
) { entry ->
    DetailsScreen(entry.arguments?.getString("id").orEmpty())
}

// adb:
// adb shell am start -a android.intent.action.VIEW -d "lab://details/42"
""".trimIndent(),
            relatedApis = listOf("navDeepLink", "uriPattern", "intent-filter", "App Links")
        ),
        RoutingTopic(
            id = "returning_results",
            title = "Returning Results",
            shortSummary = "Send data back to the previous destination",
            overview = "Sometimes a picker screen must return a value. Use SavedStateHandle on the previous back stack entry (or shared ViewModel scoped to the nav graph) instead of fragile global singletons.",
            keyPoints = listOf(
                "previousBackStackEntry?.savedStateHandle set a key before pop.",
                "Current screen collects handle.getStateFlow(key, default).",
                "Clear the key after reading to avoid sticky replays.",
                "Graph-scoped ViewModels also work for shared state.",
                "Prefer explicit keys like picker_result."
            ),
            sampleCode = """
// Picker screen — write result, then pop
val handle = navController.previousBackStackEntry?.savedStateHandle
handle?.set("picked_user", userId)
navController.popBackStack()

// Caller screen — observe result
val handle = navController.currentBackStackEntry?.savedStateHandle
val picked by handle
    ?.getStateFlow("picked_user", "")
    ?.collectAsStateWithLifecycle()
    ?: remember { mutableStateOf("") }

LaunchedEffect(picked) {
    if (picked.isNotBlank()) {
        // use value, then clear
        handle?.remove<String>("picked_user")
    }
}
""".trimIndent(),
            relatedApis = listOf("SavedStateHandle", "previousBackStackEntry", "getStateFlow", "remove")
        ),
        RoutingTopic(
            id = "typesafe_routes",
            title = "Type-Safe Routes",
            shortSummary = "@Serializable routes instead of raw strings",
            overview = "Modern Navigation Compose supports Kotlin Serialization routes so arguments are typed and refactors are safer. You navigate with objects instead of hand-built strings, reducing runtime mismatches.",
            keyPoints = listOf(
                "Annotate route objects/classes with @Serializable.",
                "composable<ProfileRoute> { ... } registers a typed destination.",
                "navigate(ProfileRoute(userId = \"42\")).",
                "Args are decoded without manual NavType boilerplate.",
                "Still think in back-stack terms — typing does not replace stack rules."
            ),
            sampleCode = """
@Serializable
data object HomeRoute

@Serializable
data class DetailsRoute(val id: String)

NavHost(navController, startDestination = HomeRoute) {
    composable<HomeRoute> {
        HomeScreen(onOpen = { id ->
            navController.navigate(DetailsRoute(id))
        })
    }
    composable<DetailsRoute> { entry ->
        val route: DetailsRoute = entry.toRoute()
        DetailsScreen(route.id)
    }
}
""".trimIndent(),
            relatedApis = listOf("@Serializable", "toRoute", "composable<T>", "type-safe navigation")
        )
    )

    fun byId(id: String): RoutingTopic? = all.find { it.id == id }

    val count: Int get() = all.size
}
