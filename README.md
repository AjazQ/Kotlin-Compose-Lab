# Kotlin Compose Lab

Interactive Android learning app for Jetpack Compose and Kotlin capabilities.

## Features

- **Level picker** — Beginner, Intermediate, Advanced
- **Filtered module dashboard** with staggered entrance animations
- **Live demos** for each topic plus a Learn tab with process steps, key APIs, and try-this tips
- **Light / Dark / System theme** toggle on the home screen

## Modules

| Level | Topics |
|-------|--------|
| Beginner | Composables & Modifiers, Layouts, State, User Input, Lazy Lists, Material 3 |
| Intermediate | State Hoisting, ViewModel + StateFlow, Coroutines, Dispatchers, Side Effects, Dynamic UI, Navigation Args, Basic Animation |
| Advanced | Structured Concurrency, Performance, Canvas, Gestures, UiState Pattern, Advanced Animation |

## Run

### Compose Live Edit (hot reload) — recommended while coding UI

1. Open this project in **Android Studio** (Ladybug / Koala or newer).
2. Start an emulator, then **Run → Debug 'app'** (bug icon), not just Run.
3. Turn on **Live Edit**:
   - Settings → Editor → Live Edit → enable **Live Edit**
   - Or use the Live Edit indicator in the running debug toolbar
4. Edit Compose UI code — changes apply on the emulator without reinstalling.

Live Edit only works while a **Debug** session is attached from Android Studio. The Cursor/`adb install` path cannot drive Studio Live Edit.

### Incremental CLI deploy (no uninstall)

Faster than a full reinstall when you are not using Live Edit:

```powershell
.\scripts\deploy-debug.ps1
```

Or:

```bash
./gradlew :app:installDebug
adb shell am start -n com.ajaz.kotlinlab/.MainActivity
```

`installDebug` recompiles only changed files and pushes an update; it still briefly restarts the activity (not the same as Live Edit).
