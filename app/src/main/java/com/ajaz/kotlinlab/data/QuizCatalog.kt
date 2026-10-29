package com.ajaz.kotlinlab.data

data class QuizQuestion(
    val id: String,
    val prompt: String,
    val choices: List<String>,
    val correctIndex: Int,
    val explanation: String
)

object QuizCatalog {
    val questions: List<QuizQuestion> = listOf(
        QuizQuestion(
            id = "q_val",
            prompt = "Which keyword declares a read-only reference in Kotlin?",
            choices = listOf("var", "val", "const", "let"),
            correctIndex = 1,
            explanation = "val creates a read-only reference. Prefer it over var when reassignment is not needed."
        ),
        QuizQuestion(
            id = "q_null",
            prompt = "What does the Elvis operator ?: do?",
            choices = listOf(
                "Force unwrap a nullable",
                "Provide a default when the left side is null",
                "Smart-cast to non-null forever",
                "Ignore nullability warnings"
            ),
            correctIndex = 1,
            explanation = "a ?: b evaluates to a when non-null, otherwise b."
        ),
        QuizQuestion(
            id = "q_when",
            prompt = "Why are sealed classes great with when?",
            choices = listOf(
                "They enable reflection",
                "They make when exhaustive at compile time",
                "They replace data classes",
                "They force an else branch always"
            ),
            correctIndex = 1,
            explanation = "All subtypes are known, so when can be exhaustive without else."
        ),
        QuizQuestion(
            id = "q_navhost",
            prompt = "What does NavHost do in Navigation Compose?",
            choices = listOf(
                "Stores Room entities",
                "Hosts destinations and swaps by back stack",
                "Replaces ViewModel",
                "Only handles deep links"
            ),
            correctIndex = 1,
            explanation = "NavHost registers composable destinations and shows the top back-stack entry."
        ),
        QuizQuestion(
            id = "q_pop",
            prompt = "What does popUpTo(route) { inclusive = true } do?",
            choices = listOf(
                "Opens a dialog",
                "Clears up to and including that destination",
                "Saves a screenshot",
                "Enables Live Edit"
            ),
            correctIndex = 1,
            explanation = "inclusive removes the popUpTo destination as well as everything above it."
        ),
        QuizQuestion(
            id = "q_path",
            prompt = "How do you declare a path argument route?",
            choices = listOf(
                "details?id",
                "details/{id}",
                "details::id",
                "details#id"
            ),
            correctIndex = 1,
            explanation = "Use curly braces: details/{id}, then navArgument + NavType."
        ),
        QuizQuestion(
            id = "q_compose_state",
            prompt = "What does remember { mutableStateOf(...) } keep across?",
            choices = listOf(
                "Process death always",
                "Recompositions of the same composition slot",
                "Only configuration changes",
                "All screens forever"
            ),
            correctIndex = 1,
            explanation = "remember survives recomposition; use rememberSaveable for rotation/process death."
        ),
        QuizQuestion(
            id = "q_retrofit",
            prompt = "How should Retrofit network calls run from a ViewModel?",
            choices = listOf(
                "On the UI thread with Thread.sleep",
                "As suspend functions inside viewModelScope.launch",
                "Only from Application.onCreate",
                "Inside @Composable without coroutines"
            ),
            correctIndex = 1,
            explanation = "Mark API methods suspend and call them from viewModelScope."
        )
    )
}
