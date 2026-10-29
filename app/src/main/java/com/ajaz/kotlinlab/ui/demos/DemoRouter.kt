package com.ajaz.kotlinlab.ui.demos

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun DemoRouter(moduleId: String, navController: NavHostController) {
    when (moduleId) {
        "composables_modifiers" -> ComposablesModifiersDemo()
        "layouts" -> LayoutsDemo()
        "basic_state" -> BasicStateDemo()
        "user_input" -> UserInputDemo()
        "lazy_lists" -> LazyListsDemo()
        "material_basics" -> MaterialBasicsDemo()
        "state_hoisting" -> StateHoistingDemo()
        "viewmodel_stateflow" -> ViewModelStateFlowDemo()
        "coroutines_compose" -> CoroutinesComposeDemo()
        "dispatchers" -> DispatchersDemo()
        "side_effects" -> SideEffectsDemo()
        "dynamic_ui" -> DynamicUiDemo()
        "navigation_args" -> NavigationArgsDemo(navController)
        "basic_animation" -> BasicAnimationDemo()
        "game_basics" -> GameBasicsDemo()
        "api_retrofit" -> ApiRetrofitDemo()
        "datastore_prefs" -> DataStorePrefsDemo()
        "room_storage" -> RoomStorageDemo()
        "structured_concurrency" -> StructuredConcurrencyDemo()
        "performance" -> PerformanceDemo()
        "custom_drawing" -> CustomDrawingDemo()
        "gestures" -> GesturesDemo()
        "ui_state_pattern" -> UiStatePatternDemo()
        "advanced_animation" -> AdvancedAnimationDemo()
        else -> Text("Demo not found for $moduleId")
    }
}
