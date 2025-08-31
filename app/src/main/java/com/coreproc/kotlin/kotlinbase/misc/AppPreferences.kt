package com.coreproc.kotlin.kotlinbase.misc

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Extension property to create DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

@Singleton
class AppPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val API_KEY = stringPreferencesKey("api_key")
    private val PREF_NAME = "app_preferences_sync"

    // Synchronous SharedPreferences for interceptor use (legacy support)
    private val syncPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // Modern DataStore methods for general app use
    suspend fun saveApiKey(key: String) {
        // Save to both DataStore and SharedPreferences for compatibility
        context.dataStore.edit { preferences ->
            preferences[API_KEY] = key
        }
        syncPreferences.edit {
            putString("api_key", key)
        }
    }

    suspend fun getApiKey(): String? {
        return context.dataStore.data.map { preferences ->
            preferences[API_KEY]
        }.first()
    }

    // Synchronous method for interceptor use
    fun getApiKeySync(): String? {
        return syncPreferences.getString("api_key", null)
    }

    fun getApiKeyFlow(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[API_KEY]
        }
    }

    suspend fun clearApiKey() {
        context.dataStore.edit { preferences ->
            preferences.remove(API_KEY)
        }
        syncPreferences.edit {
            remove("api_key")
        }
    }

    suspend fun hasApiKey(): Boolean {
        return !getApiKey().isNullOrEmpty()
    }

    fun hasApiKeyFlow(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            !preferences[API_KEY].isNullOrEmpty()
        }
    }

    suspend fun logout() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
        syncPreferences.edit {
            clear()
        }
    }
}