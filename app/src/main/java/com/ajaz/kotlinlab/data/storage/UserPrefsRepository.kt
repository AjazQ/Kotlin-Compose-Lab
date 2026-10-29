package com.ajaz.kotlinlab.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "lab_prefs")

class UserPrefsRepository(private val context: Context) {
    private val nameKey = stringPreferencesKey("display_name")
    private val darkHintKey = booleanPreferencesKey("prefer_dark_hint")

    val displayName: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[nameKey].orEmpty()
    }

    val preferDarkHint: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[darkHintKey] ?: false
    }

    suspend fun setDisplayName(value: String) {
        context.dataStore.edit { prefs ->
            prefs[nameKey] = value
        }
    }

    suspend fun setPreferDarkHint(value: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[darkHintKey] = value
        }
    }
}
