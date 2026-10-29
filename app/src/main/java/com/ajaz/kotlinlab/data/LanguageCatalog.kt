package com.ajaz.kotlinlab.data

object LanguageCatalog {
    val all: List<LanguageTopic> = listOf(
        LanguageTopic(
            id = "variables_types",
            title = "Variables & Types",
            shortSummary = "val, var, type inference, and basic types",
            overview = "Kotlin prefers immutability. Use val for read-only references and var when reassignment is required. The compiler usually infers types; you can still declare them explicitly.",
            keyPoints = listOf(
                "val = read-only reference (preferred).",
                "var = mutable reference.",
                "Common types: Int, Long, Double, Float, Boolean, Char, String.",
                "Type inference: val name = \"Ajaz\" is String.",
                "Numbers do not silently widen; use toInt()/toLong() when needed."
            ),
            sampleCode = """
fun main() {
    val answer: Int = 42          // immutable
    var counter = 0               // inferred Int
    counter += 1

    val pi = 3.14                 // Double
    val flag = true               // Boolean
    val label = "Kotlin Lab"      // String

    println("${'$'}answer ${'$'}counter ${'$'}pi ${'$'}flag ${'$'}label")
}
""".trimIndent(),
            relatedApis = listOf("val", "var", "Int", "String", "Boolean", "type inference")
        ),
        LanguageTopic(
            id = "null_safety",
            title = "Null Safety",
            shortSummary = "?, !!, ?., ?: and safe casts",
            overview = "Kotlin's type system distinguishes nullable and non-null types. This removes most NullPointerExceptions at compile time. Prefer safe calls and Elvis over !! unless you are certain.",
            keyPoints = listOf(
                "String cannot hold null; String? can.",
                "?. calls only if the receiver is non-null.",
                "?: provides a default when the left side is null (Elvis).",
                "!! asserts non-null and throws if null — use sparingly.",
                "as? is a safe cast that returns null on failure."
            ),
            sampleCode = """
fun lengthOrZero(text: String?): Int {
    return text?.length ?: 0
}

fun main() {
    val name: String? = null
    println(name?.uppercase())     // null
    println(lengthOrZero(name))    // 0

    val value: Any = "42"
    val number = value as? Int     // null (safe cast)
    println(number ?: "not an Int")
}
""".trimIndent(),
            relatedApis = listOf("?", "?.", "?:", "!!", "as?", "nullable types")
        ),
        LanguageTopic(
            id = "functions_lambdas",
            title = "Functions & Lambdas",
            shortSummary = "Named params, defaults, higher-order functions",
            overview = "Functions are first-class. You can pass lambdas to other functions, use default arguments, and call with named parameters for clarity.",
            keyPoints = listOf(
                "fun name(params): ReturnType { ... }",
                "Default arguments reduce overloads.",
                "Named arguments document call sites.",
                "Lambdas: { x -> x * 2 } or { it * 2 }.",
                "Trailing lambda syntax: list.filter { it > 0 }."
            ),
            sampleCode = """
fun greet(name: String, excitedly: Boolean = false): String {
    return if (excitedly) "Hello, ${'$'}name!" else "Hello, ${'$'}name"
}

fun transform(value: Int, block: (Int) -> Int): Int = block(value)

fun main() {
    println(greet(name = "Ada"))
    println(greet("Ada", excitedly = true))

    val doubled = transform(21) { it * 2 }
    println(doubled) // 42

    val nums = listOf(1, 2, 3, 4)
    println(nums.filter { it % 2 == 0 })
}
""".trimIndent(),
            relatedApis = listOf("fun", "lambda", "it", "trailing lambda", "default args")
        ),
        LanguageTopic(
            id = "classes_objects",
            title = "Classes & Objects",
            shortSummary = "Primary constructors, properties, object, companion",
            overview = "Kotlin classes are concise. The primary constructor can declare properties. object creates a singleton; companion object holds 'static-like' members.",
            keyPoints = listOf(
                "class User(val name: String) declares a property in the constructor.",
                "init { } runs initialization logic.",
                "object Logger { } is a singleton.",
                "companion object members are called like User.create().",
                "Classes are final by default; mark open to allow inheritance."
            ),
            sampleCode = """
class User(val name: String, var age: Int) {
    init {
        require(age >= 0) { "age must be >= 0" }
    }

    fun describe() = "${'$'}name (${'$'}age)"

    companion object {
        fun guest() = User("Guest", 0)
    }
}

object AppConfig {
    const val APP_NAME = "Kotlin Compose Lab"
}

fun main() {
    val u = User("Sam", 30)
    println(u.describe())
    println(User.guest().describe())
    println(AppConfig.APP_NAME)
}
""".trimIndent(),
            relatedApis = listOf("class", "init", "object", "companion object", "open")
        ),
        LanguageTopic(
            id = "data_classes",
            title = "Data Classes",
            shortSummary = "equals, copy, destructuring for models",
            overview = "data class generates equals/hashCode/toString/copy and componentN for destructuring. Ideal for immutable UI/domain models.",
            keyPoints = listOf(
                "Must have at least one primary constructor property.",
                "copy() clones with selective overrides.",
                "Destructuring: val (id, title) = item.",
                "Prefer val properties for immutable models.",
                "Do not put heavy logic inside data classes."
            ),
            sampleCode = """
data class Todo(
    val id: Int,
    val title: String,
    val done: Boolean = false
)

fun main() {
    val a = Todo(1, "Learn Kotlin")
    val b = a.copy(done = true)
    println(a)
    println(b)
    println(a == b) // false

    val (id, title) = b
    println("${'$'}id -> ${'$'}title")
}
""".trimIndent(),
            relatedApis = listOf("data class", "copy", "equals", "destructuring")
        ),
        LanguageTopic(
            id = "when_expression",
            title = "When Expression",
            shortSummary = "Powerful replacement for switch",
            overview = "when can replace switch and complex if-else chains. As an expression it must be exhaustive for sealed types/enums, which makes refactors safer.",
            keyPoints = listOf(
                "Can match values, ranges, types, and arbitrary conditions.",
                "Used as expression: val x = when (...) { ... }.",
                "Smart casts work in branches (is String).",
                "Sealed class when is exhaustive without else.",
                "else handles remaining cases for open types."
            ),
            sampleCode = """
fun labelFor(score: Int): String = when (score) {
    in 90..100 -> "A"
    in 80..89 -> "B"
    in 70..79 -> "C"
    else -> "Keep practicing"
}

fun describe(value: Any): String = when (value) {
    is String -> "String length=${'$'}{value.length}"
    is Int -> "Int squared=${'$'}{value * value}"
    else -> "Unknown"
}

fun main() {
    println(labelFor(92))
    println(describe("Kotlin"))
}
""".trimIndent(),
            relatedApis = listOf("when", "is", "in", "exhaustive when")
        ),
        LanguageTopic(
            id = "collections",
            title = "Collections",
            shortSummary = "List, Set, Map and functional operators",
            overview = "Kotlin distinguishes read-only and mutable collections. Prefer List/Set/Map APIs plus map/filter/flatMap for declarative transforms.",
            keyPoints = listOf(
                "listOf / mutableListOf, setOf / mutableSetOf, mapOf / mutableMapOf.",
                "Read-only interfaces do not guarantee immutability deep down.",
                "filter, map, flatMap, groupBy, fold are common.",
                "sequences (asSequence) are lazy for large pipelines.",
                "Prefer immutable snapshots in UI state."
            ),
            sampleCode = """
fun main() {
    val nums = listOf(1, 2, 3, 4, 5, 6)
    val evensSquared = nums
        .filter { it % 2 == 0 }
        .map { it * it }
    println(evensSquared) // [4, 16, 36]

    val scores = mapOf("Ada" to 98, "Lin" to 90)
    println(scores["Ada"])

    val mutable = mutableListOf("a", "b")
    mutable += "c"
    println(mutable)
}
""".trimIndent(),
            relatedApis = listOf("listOf", "mapOf", "filter", "map", "mutableListOf", "Sequence")
        ),
        LanguageTopic(
            id = "extensions",
            title = "Extension Functions",
            shortSummary = "Add APIs to existing types without inheritance",
            overview = "Extensions let you write helper functions as if they were members of a type. They are resolved statically and are perfect for readable utilities.",
            keyPoints = listOf(
                "Syntax: fun Type.name(params): ReturnType.",
                "Inside the function, this is the receiver.",
                "Extensions do not actually modify the class bytecode members.",
                "Nullable receivers are allowed: fun String?.orEmpty().",
                "Keep them cohesive; avoid polluting common types globally."
            ),
            sampleCode = """
fun String.initials(): String =
    trim()
        .split(" ")
        .filter { it.isNotBlank() }
        .joinToString("") { it.first().uppercase() }

fun Int.isEven(): Boolean = this % 2 == 0

fun main() {
    println("Grace Hopper".initials()) // GH
    println(4.isEven())
}
""".trimIndent(),
            relatedApis = listOf("extension function", "receiver", "this")
        ),
        LanguageTopic(
            id = "scope_functions",
            title = "Scope Functions",
            shortSummary = "let, run, with, apply, also",
            overview = "Scope functions execute a block in the context of an object. Choose by whether you need the object as this or it, and whether the block returns the object or a result.",
            keyPoints = listOf(
                "let / also use it; run / apply use this.",
                "let/run/with return the lambda result.",
                "apply/also return the original object.",
                "Typical: also for side effects, apply for configuration, let for null-safe transforms.",
                "Do not nest many scope functions — readability suffers."
            ),
            sampleCode = """
data class Config(var host: String = "", var port: Int = 0)

fun main() {
    val config = Config().apply {
        host = "localhost"
        port = 8080
    }

    val label = config.let { "${'$'}{it.host}:${'$'}{it.port}" }
    println(label)

    config.also { println("Configured ${'$'}it") }
}
""".trimIndent(),
            relatedApis = listOf("let", "run", "with", "apply", "also")
        ),
        LanguageTopic(
            id = "sealed_classes",
            title = "Sealed Classes & Interfaces",
            shortSummary = "Restricted hierarchies for UiState modeling",
            overview = "Sealed types limit subclasses to a known set (usually same module). Combined with when, you get compile-time exhaustiveness — ideal for Loading/Success/Error UI state.",
            keyPoints = listOf(
                "sealed class / sealed interface.",
                "All direct subtypes are known to the compiler.",
                "Perfect for modeling finite states and results.",
                "when on sealed types can omit else if exhaustive.",
                "Prefer immutable data objects/classes as subtypes."
            ),
            sampleCode = """
sealed interface Result {
    data object Loading : Result
    data class Ok(val data: String) : Result
    data class Err(val message: String) : Result
}

fun render(result: Result): String = when (result) {
    Result.Loading -> "Loading…"
    is Result.Ok -> result.data
    is Result.Err -> "Error: ${'$'}{result.message}"
}

fun main() {
    println(render(Result.Loading))
    println(render(Result.Ok("Done")))
}
""".trimIndent(),
            relatedApis = listOf("sealed class", "sealed interface", "when", "data object")
        ),
        LanguageTopic(
            id = "generics_basics",
            title = "Generics Basics",
            shortSummary = "Type parameters, constraints, and variance intro",
            overview = "Generics let you write reusable containers and functions. Use constraints like T : Comparable<T> when you need capabilities. Variance (in/out) matters for producer/consumer APIs.",
            keyPoints = listOf(
                "class Box<T>(val value: T).",
                "fun <T> singletonList(item: T): List<T>.",
                "Constraints: <T : Number>.",
                "out T ≈ producer (covariant), in T ≈ consumer (contravariant).",
                "star projection T<*> when type is unknown."
            ),
            sampleCode = """
class Box<T>(val value: T) {
    fun unwrap(): T = value
}

fun <T> List<T>.secondOrNull(): T? = if (size >= 2) this[1] else null

fun <T : Comparable<T>> maxOfTwo(a: T, b: T): T =
    if (a >= b) a else b

fun main() {
    val box = Box("Kotlin")
    println(box.unwrap())
    println(listOf(10, 20, 30).secondOrNull())
    println(maxOfTwo(3, 9))
}
""".trimIndent(),
            relatedApis = listOf("<T>", "type parameter", "constraint", "out", "in")
        ),
        LanguageTopic(
            id = "coroutines_language",
            title = "Coroutines Language Basics",
            shortSummary = "suspend functions and structured concurrency idea",
            overview = "suspend functions can pause without blocking threads. They must be called from a coroutine or another suspend function. Structured concurrency keeps child work scoped to a parent job.",
            keyPoints = listOf(
                "Mark async APIs with suspend.",
                "Start coroutines with launch/async in a CoroutineScope.",
                "delay() suspends; Thread.sleep() blocks.",
                "withContext switches dispatcher (Main/IO/Default).",
                "Cancellation is cooperative via suspend points."
            ),
            sampleCode = """
import kotlinx.coroutines.*

suspend fun fetchMessage(): String {
    delay(300) // non-blocking wait
    return "Hello from coroutine"
}

fun main() = runBlocking {
    val job = launch {
        println(fetchMessage())
    }
    job.join()
}
""".trimIndent(),
            relatedApis = listOf("suspend", "launch", "async", "delay", "CoroutineScope", "withContext")
        )
    )

    fun byId(id: String): LanguageTopic? = all.find { it.id == id }

    val count: Int get() = all.size
}
