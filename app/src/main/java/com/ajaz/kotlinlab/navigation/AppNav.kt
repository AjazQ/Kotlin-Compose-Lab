package com.ajaz.kotlinlab.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ajaz.kotlinlab.data.LanguageCatalog
import com.ajaz.kotlinlab.data.ModuleCatalog
import com.ajaz.kotlinlab.data.RoutingCatalog
import com.ajaz.kotlinlab.data.SearchHit
import com.ajaz.kotlinlab.data.SkillLevel
import com.ajaz.kotlinlab.data.storage.ContentKind
import com.ajaz.kotlinlab.data.storage.LearningProgressRepository
import com.ajaz.kotlinlab.ui.dashboard.DashboardScreen
import com.ajaz.kotlinlab.ui.demos.DemoRouter
import com.ajaz.kotlinlab.ui.demos.NavigationArgsDetailDemo
import com.ajaz.kotlinlab.ui.favorites.FavoritesScreen
import com.ajaz.kotlinlab.ui.home.HomeScreen
import com.ajaz.kotlinlab.ui.language.LanguageGuideScreen
import com.ajaz.kotlinlab.ui.language.LanguageTopicScreen
import com.ajaz.kotlinlab.ui.module.ModuleScaffold
import com.ajaz.kotlinlab.ui.quiz.QuizScreen
import com.ajaz.kotlinlab.ui.routing.RoutingGuideScreen
import com.ajaz.kotlinlab.ui.routing.RoutingTopicScreen
import com.ajaz.kotlinlab.ui.search.SearchScreen
import com.ajaz.kotlinlab.ui.splash.StartupScreen
import com.ajaz.kotlinlab.ui.theme.MotionTokens
import com.ajaz.kotlinlab.ui.theme.ThemeMode
import kotlinx.coroutines.launch

object Routes {
    const val Splash = "splash"
    const val Home = "home"
    const val Dashboard = "dashboard/{level}"
    const val Module = "module/{id}"
    const val NavArgsDetail = "nav_args_detail/{message}"
    const val LanguageGuide = "language_guide"
    const val LanguageTopic = "language_topic/{id}"
    const val RoutingGuide = "routing_guide"
    const val RoutingTopic = "routing_topic/{id}"
    const val Search = "search"
    const val Favorites = "favorites"
    const val Quiz = "quiz"

    fun dashboard(level: SkillLevel) = "dashboard/${level.name}"
    fun module(id: String) = "module/$id"
    fun languageTopic(id: String) = "language_topic/$id"
    fun routingTopic(id: String) = "routing_topic/$id"
}

@Composable
fun AppNav(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val progressRepo = remember { LearningProgressRepository(context) }
    val progress by progressRepo.snapshot.collectAsStateWithLifecycle(
        initialValue = com.ajaz.kotlinlab.data.storage.ProgressSnapshot()
    )
    val scope = rememberCoroutineScope()
    val mediumFade = tween<Float>(MotionTokens.Medium)
    val mediumSlide = tween<IntOffset>(MotionTokens.Medium)

    fun openHit(hit: SearchHit) {
        when (hit.kind) {
            ContentKind.MODULE -> navController.navigate(Routes.module(hit.id))
            ContentKind.LANGUAGE -> navController.navigate(Routes.languageTopic(hit.id))
            ContentKind.ROUTING -> navController.navigate(Routes.routingTopic(hit.id))
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.Splash,
        modifier = modifier,
        enterTransition = {
            fadeIn(mediumFade) + slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = mediumSlide
            )
        },
        exitTransition = { fadeOut(tween(MotionTokens.Short)) },
        popEnterTransition = {
            fadeIn(mediumFade) + slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = mediumSlide
            )
        },
        popExitTransition = {
            fadeOut(tween(MotionTokens.Short)) + slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = mediumSlide
            )
        }
    ) {
        composable(
            route = Routes.Splash,
            enterTransition = { fadeIn(tween(0)) },
            exitTransition = { fadeOut(tween(450)) }
        ) {
            StartupScreen(
                onFinished = {
                    navController.navigate(Routes.Home) {
                        popUpTo(Routes.Splash) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.Home) {
            HomeScreen(
                themeMode = themeMode,
                onThemeModeChange = onThemeModeChange,
                progress = progress,
                onLevelSelected = { navController.navigate(Routes.dashboard(it)) },
                onLanguageGuideClick = { navController.navigate(Routes.LanguageGuide) },
                onRoutingGuideClick = { navController.navigate(Routes.RoutingGuide) },
                onSearchClick = { navController.navigate(Routes.Search) },
                onFavoritesClick = { navController.navigate(Routes.Favorites) },
                onQuizClick = { navController.navigate(Routes.Quiz) }
            )
        }
        composable(Routes.Search) {
            SearchScreen(
                onBack = { navController.popBackStack() },
                onOpenHit = { openHit(it) }
            )
        }
        composable(Routes.Favorites) {
            FavoritesScreen(
                progress = progress,
                onBack = { navController.popBackStack() },
                onOpenHit = { openHit(it) }
            )
        }
        composable(Routes.Quiz) {
            QuizScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.LanguageGuide) {
            LanguageGuideScreen(
                onBack = { navController.popBackStack() },
                onTopicClick = { navController.navigate(Routes.languageTopic(it)) }
            )
        }
        composable(
            route = Routes.LanguageTopic,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { entry ->
            val id = entry.arguments?.getString("id").orEmpty()
            val topic = LanguageCatalog.byId(id)
            if (topic == null) {
                LanguageGuideScreen(
                    onBack = { navController.popBackStack() },
                    onTopicClick = { navController.navigate(Routes.languageTopic(it)) }
                )
            } else {
                LanguageTopicScreen(
                    topic = topic,
                    isCompleted = progressRepo.isCompleted(progress, ContentKind.LANGUAGE, id),
                    isFavorite = progressRepo.isFavorite(progress, ContentKind.LANGUAGE, id),
                    onToggleComplete = {
                        scope.launch { progressRepo.toggleCompleted(ContentKind.LANGUAGE, id) }
                    },
                    onToggleFavorite = {
                        scope.launch { progressRepo.toggleFavorite(ContentKind.LANGUAGE, id) }
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }
        composable(Routes.RoutingGuide) {
            RoutingGuideScreen(
                onBack = { navController.popBackStack() },
                onTopicClick = { navController.navigate(Routes.routingTopic(it)) }
            )
        }
        composable(
            route = Routes.RoutingTopic,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { entry ->
            val id = entry.arguments?.getString("id").orEmpty()
            val topic = RoutingCatalog.byId(id)
            if (topic == null) {
                RoutingGuideScreen(
                    onBack = { navController.popBackStack() },
                    onTopicClick = { navController.navigate(Routes.routingTopic(it)) }
                )
            } else {
                RoutingTopicScreen(
                    topic = topic,
                    isCompleted = progressRepo.isCompleted(progress, ContentKind.ROUTING, id),
                    isFavorite = progressRepo.isFavorite(progress, ContentKind.ROUTING, id),
                    onToggleComplete = {
                        scope.launch { progressRepo.toggleCompleted(ContentKind.ROUTING, id) }
                    },
                    onToggleFavorite = {
                        scope.launch { progressRepo.toggleFavorite(ContentKind.ROUTING, id) }
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }
        composable(
            route = Routes.Dashboard,
            arguments = listOf(navArgument("level") { type = NavType.StringType })
        ) { entry ->
            val level = SkillLevel.fromRoute(entry.arguments?.getString("level").orEmpty())
            DashboardScreen(
                level = level,
                onBack = { navController.popBackStack() },
                onModuleClick = { navController.navigate(Routes.module(it)) }
            )
        }
        composable(
            route = Routes.Module,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { entry ->
            val id = entry.arguments?.getString("id").orEmpty()
            val module = ModuleCatalog.byId(id)
            if (module == null) {
                HomeScreen(
                    themeMode = themeMode,
                    onThemeModeChange = onThemeModeChange,
                    progress = progress,
                    onLevelSelected = { navController.navigate(Routes.dashboard(it)) },
                    onLanguageGuideClick = { navController.navigate(Routes.LanguageGuide) },
                    onRoutingGuideClick = { navController.navigate(Routes.RoutingGuide) },
                    onSearchClick = { navController.navigate(Routes.Search) },
                    onFavoritesClick = { navController.navigate(Routes.Favorites) },
                    onQuizClick = { navController.navigate(Routes.Quiz) }
                )
            } else {
                ModuleScaffold(
                    module = module,
                    isCompleted = progressRepo.isCompleted(progress, ContentKind.MODULE, id),
                    isFavorite = progressRepo.isFavorite(progress, ContentKind.MODULE, id),
                    onToggleComplete = {
                        scope.launch { progressRepo.toggleCompleted(ContentKind.MODULE, id) }
                    },
                    onToggleFavorite = {
                        scope.launch { progressRepo.toggleFavorite(ContentKind.MODULE, id) }
                    },
                    onBack = { navController.popBackStack() },
                    demoContent = { DemoRouter(moduleId = module.id, navController = navController) }
                )
            }
        }
        composable(
            route = Routes.NavArgsDetail,
            arguments = listOf(navArgument("message") { type = NavType.StringType })
        ) { entry ->
            val message = entry.arguments?.getString("message").orEmpty()
            val module = ModuleCatalog.byId("navigation_args")!!
            ModuleScaffold(
                module = module,
                isCompleted = progressRepo.isCompleted(progress, ContentKind.MODULE, module.id),
                isFavorite = progressRepo.isFavorite(progress, ContentKind.MODULE, module.id),
                onToggleComplete = {
                    scope.launch { progressRepo.toggleCompleted(ContentKind.MODULE, module.id) }
                },
                onToggleFavorite = {
                    scope.launch { progressRepo.toggleFavorite(ContentKind.MODULE, module.id) }
                },
                onBack = { navController.popBackStack() },
                demoContent = {
                    NavigationArgsDetailDemo(
                        message = message,
                        onBack = { navController.popBackStack() }
                    )
                }
            )
        }
    }
}
