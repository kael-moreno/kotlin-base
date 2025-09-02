package com.coreproc.kotlin.kotlinbase.misc

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.coreproc.kotlin.kotlinbase.security.KeystoreManager
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
    @ApplicationContext private val context: Context,
    private val keystoreManager: KeystoreManager
) {
    private val apiKeyPreferences = stringPreferencesKey("api_key")
    private val appPreferencesSync = "app_preferences_sync"
    private val keystoreAlias = context.packageName

    // Synchronous SharedPreferences for interceptor use (legacy support)
    private val syncPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(appPreferencesSync, Context.MODE_PRIVATE)
    }

    init {
        // Generate the encryption key for API keys
        keystoreManager.generateSecretKey(keystoreAlias)
    }

    // Modern DataStore methods for general app use (with encryption)
    suspend fun saveApiKey(key: String) {
        val encryptedKey = keystoreManager.encryptData(key, keystoreAlias)
        if (encryptedKey != null) {
            // Save encrypted data to DataStore
            context.dataStore.edit { preferences ->
                preferences[apiKeyPreferences] = encryptedKey
            }
            // Save encrypted data to SharedPreferences for interceptor compatibility
            syncPreferences.edit {
                putString(apiKeyPreferences.name, encryptedKey)
            }
        }
    }

    suspend fun getApiKey(): String? {
        val encryptedKey = context.dataStore.data.map { preferences ->
            preferences[apiKeyPreferences]
        }.first()

        return encryptedKey?.let { keystoreManager.decryptData(it, keystoreAlias) }
    }

    // Synchronous method for interceptor use (with decryption)
    fun getApiKeySync(): String? {
        val encryptedKey = syncPreferences.getString(apiKeyPreferences.name, null)
        return encryptedKey?.let { keystoreManager.decryptData(it, keystoreAlias) }
    }

    fun getApiKeyFlow(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[apiKeyPreferences]?.let { keystoreManager.decryptData(it, keystoreAlias) }
        }
    }

    suspend fun clearApiKey() {
        context.dataStore.edit { preferences ->
            preferences.remove(apiKeyPreferences)
        }
        syncPreferences.edit {
            remove(apiKeyPreferences.name)
        }
    }

    suspend fun hasApiKey(): Boolean {
        return !getApiKey().isNullOrEmpty()
    }

    fun hasApiKeyFlow(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            val encryptedKey = preferences[apiKeyPreferences]
            !encryptedKey.isNullOrEmpty() && !keystoreManager.decryptData(encryptedKey, keystoreAlias).isNullOrEmpty()
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