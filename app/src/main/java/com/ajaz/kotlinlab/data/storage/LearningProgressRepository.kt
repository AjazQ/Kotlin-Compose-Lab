package com.ajaz.kotlinlab.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ajaz.kotlinlab.data.LanguageCatalog
import com.ajaz.kotlinlab.data.ModuleCatalog
import com.ajaz.kotlinlab.data.RoutingCatalog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.progressStore: DataStore<Preferences> by preferencesDataStore(name = "lab_progress")

enum class ContentKind { MODULE, LANGUAGE, ROUTING }

data class ProgressSnapshot(
    val completedModules: Set<String> = emptySet(),
    val completedLanguage: Set<String> = emptySet(),
    val completedRouting: Set<String> = emptySet(),
    val favorites: Set<String> = emptySet()
) {
    val moduleTotal: Int get() = ModuleCatalog.all.size
    val languageTotal: Int get() = LanguageCatalog.count
    val routingTotal: Int get() = RoutingCatalog.count
    val modulesDone: Int get() = completedModules.size.coerceAtMost(moduleTotal)
    val languageDone: Int get() = completedLanguage.size.coerceAtMost(languageTotal)
    val routingDone: Int get() = completedRouting.size.coerceAtMost(routingTotal)
    val overallDone: Int get() = modulesDone + languageDone + routingDone
    val overallTotal: Int get() = moduleTotal + languageTotal + routingTotal
    val overallFraction: Float
        get() = if (overallTotal == 0) 0f else overallDone.toFloat() / overallTotal.toFloat()
}

class LearningProgressRepository(private val context: Context) {
    private val modulesKey = stringSetPreferencesKey("completed_modules")
    private val languageKey = stringSetPreferencesKey("completed_language")
    private val routingKey = stringSetPreferencesKey("completed_routing")
    private val favoritesKey = stringSetPreferencesKey("favorites")

    val snapshot: Flow<ProgressSnapshot> = context.progressStore.data.map { prefs ->
        ProgressSnapshot(
            completedModules = prefs[modulesKey].orEmpty(),
            completedLanguage = prefs[languageKey].orEmpty(),
            completedRouting = prefs[routingKey].orEmpty(),
            favorites = prefs[favoritesKey].orEmpty()
        )
    }

    suspend fun toggleCompleted(kind: ContentKind, id: String) {
        val key = when (kind) {
            ContentKind.MODULE -> modulesKey
            ContentKind.LANGUAGE -> languageKey
            ContentKind.ROUTING -> routingKey
        }
        context.progressStore.edit { prefs ->
            val set = prefs[key].orEmpty().toMutableSet()
            if (!set.add(id)) set.remove(id)
            prefs[key] = set
        }
    }

    suspend fun markCompleted(kind: ContentKind, id: String) {
        val key = when (kind) {
            ContentKind.MODULE -> modulesKey
            ContentKind.LANGUAGE -> languageKey
            ContentKind.ROUTING -> routingKey
        }
        context.progressStore.edit { prefs ->
            prefs[key] = prefs[key].orEmpty() + id
        }
    }

    fun favoriteKey(kind: ContentKind, id: String) = "${kind.name}:$id"

    suspend fun toggleFavorite(kind: ContentKind, id: String) {
        val token = favoriteKey(kind, id)
        context.progressStore.edit { prefs ->
            val set = prefs[favoritesKey].orEmpty().toMutableSet()
            if (!set.add(token)) set.remove(token)
            prefs[favoritesKey] = set
        }
    }

    fun isCompleted(snapshot: ProgressSnapshot, kind: ContentKind, id: String): Boolean =
        when (kind) {
            ContentKind.MODULE -> id in snapshot.completedModules
            ContentKind.LANGUAGE -> id in snapshot.completedLanguage
            ContentKind.ROUTING -> id in snapshot.completedRouting
        }

    fun isFavorite(snapshot: ProgressSnapshot, kind: ContentKind, id: String): Boolean =
        favoriteKey(kind, id) in snapshot.favorites
}
