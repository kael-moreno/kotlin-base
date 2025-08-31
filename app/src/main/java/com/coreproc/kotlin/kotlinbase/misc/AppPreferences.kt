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
    private const val API_KEY = stringPreferencesKey("api_key")
    private const val PREF_NAME = "app_preferences_sync"
    private const val API_KEY_ALIAS = "AppPreferencesApiKey"

    // Synchronous SharedPreferences for interceptor use (legacy support)
    private val syncPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    init {
        // Generate the encryption key for API keys
        keystoreManager.generateSecretKey(API_KEY_ALIAS)
    }

    // Modern DataStore methods for general app use (with encryption)
    suspend fun saveApiKey(key: String) {
        val encryptedKey = keystoreManager.encryptData(key, API_KEY_ALIAS)
        if (encryptedKey != null) {
            // Save encrypted data to DataStore
            context.dataStore.edit { preferences ->
                preferences[API_KEY] = encryptedKey
            }
            // Save encrypted data to SharedPreferences for interceptor compatibility
            syncPreferences.edit {
                putString(API_KEY.name, encryptedKey)
            }
        }
    }

    suspend fun getApiKey(): String? {
        val encryptedKey = context.dataStore.data.map { preferences ->
            preferences[API_KEY]
        }.first()

        return encryptedKey?.let { keystoreManager.decryptData(it, API_KEY_ALIAS) }
    }

    // Synchronous method for interceptor use (with decryption)
    fun getApiKeySync(): String? {
        val encryptedKey = syncPreferences.getString(API_KEY.name, null)
        return encryptedKey?.let { keystoreManager.decryptData(it, API_KEY_ALIAS) }
    }

    fun getApiKeyFlow(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[API_KEY]?.let { keystoreManager.decryptData(it, API_KEY_ALIAS) }
        }
    }

    suspend fun clearApiKey() {
        context.dataStore.edit { preferences ->
            preferences.remove(API_KEY)
        }
        syncPreferences.edit {
            remove(API_KEY.name)
        }
    }

    suspend fun hasApiKey(): Boolean {
        return !getApiKey().isNullOrEmpty()
    }

    fun hasApiKeyFlow(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            val encryptedKey = preferences[API_KEY]
            !encryptedKey.isNullOrEmpty() && !keystoreManager.decryptData(encryptedKey, API_KEY_ALIAS).isNullOrEmpty()
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