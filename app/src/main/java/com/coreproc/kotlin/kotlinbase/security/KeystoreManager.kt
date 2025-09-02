package com.coreproc.kotlin.kotlinbase.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import timber.log.Timber
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KeystoreManager @Inject constructor() {

    private val ANDROID_KEYSTORE = "AndroidKeyStore"
    private val AES_MODE = "AES/GCM/NoPadding"
    private val IV_SEPARATOR = ":"

    private val keyStore: KeyStore by lazy {
        KeyStore.getInstance(ANDROID_KEYSTORE).apply {
            load(null)
        }
    }

    /**
     * Generates a hardware-backed AES key in Android Keystore
     * @param alias Unique identifier for the key
     * @param requireAuthentication Whether to require user authentication to use the key
     */
    fun generateSecretKey(alias: String, requireAuthentication: Boolean = false) {
        try {
            if (!keyStore.containsAlias(alias)) {
                val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
                val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                    alias,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setRandomizedEncryptionRequired(true)
                    .apply {
                        if (requireAuthentication) {
                            setUserAuthenticationRequired(true)
                            // For modern biometric authentication, use setUserAuthenticationParameters
                            // This requires authentication for each use
                        }
                    }
                    .build()
                keyGenerator.init(keyGenParameterSpec)
                keyGenerator.generateKey()
                Timber.d("Secret key generated for alias: $alias")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error generating secret key for alias: $alias")
        }
    }

    /**
     * Retrieves the secret key from Android Keystore
     * @param alias The key alias
     * @return SecretKey or null if not found
     */
    private fun getSecretKey(alias: String): SecretKey? {
        return try {
            keyStore.getKey(alias, null) as SecretKey
        } catch (e: Exception) {
            Timber.e(e, "Error getting secret key for alias: $alias")
            null
        }
    }

    /**
     * Encrypts data using hardware-backed AES key
     * @param data The plaintext data to encrypt
     * @param alias The key alias to use for encryption
     * @return Base64 encoded encrypted data with IV, or null if encryption fails
     */
    fun encryptData(data: String, alias: String): String? {
        return try {
            val secretKey = getSecretKey(alias) ?: return null
            val cipher = Cipher.getInstance(AES_MODE)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)

            val iv = cipher.iv
            val encryptedData = cipher.doFinal(data.toByteArray())

            // Combine IV and encrypted data for storage
            val ivString = Base64.encodeToString(iv, Base64.DEFAULT)
            val encryptedString = Base64.encodeToString(encryptedData, Base64.DEFAULT)

            "$ivString$IV_SEPARATOR$encryptedString"
        } catch (e: Exception) {
            Timber.e(e, "Error encrypting data with alias: $alias")
            null
        }
    }

    /**
     * Decrypts data using hardware-backed AES key
     * @param encryptedData Base64 encoded encrypted data with IV
     * @param alias The key alias to use for decryption
     * @return Decrypted plaintext data, or null if decryption fails
     */
    fun decryptData(encryptedData: String, alias: String): String? {
        return try {
            val secretKey = getSecretKey(alias) ?: return null
            val parts = encryptedData.split(IV_SEPARATOR)
            if (parts.size != 2) return null

            val iv = Base64.decode(parts[0], Base64.DEFAULT)
            val encrypted = Base64.decode(parts[1], Base64.DEFAULT)

            val cipher = Cipher.getInstance(AES_MODE)
            val spec = GCMParameterSpec(128, iv)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

            val decryptedData = cipher.doFinal(encrypted)
            String(decryptedData)
        } catch (e: Exception) {
            Timber.e(e, "Error decrypting data with alias: $alias")
            null
        }
    }

    /**
     * Checks if a key exists in the keystore
     * @param alias The key alias to check
     * @return true if key exists, false otherwise
     */
    fun hasKey(alias: String): Boolean {
        return try {
            keyStore.containsAlias(alias)
        } catch (e: Exception) {
            Timber.e(e, "Error checking key existence for alias: $alias")
            false
        }
    }

    /**
     * Deletes a key from the keystore
     * @param alias The key alias to delete
     */
    fun deleteKey(alias: String) {
        try {
            if (keyStore.containsAlias(alias)) {
                keyStore.deleteEntry(alias)
                Timber.d("Key deleted for alias: $alias")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error deleting key for alias: $alias")
        }
    }

    /**
     * Convenience method to encrypt data with a default key
     * Automatically generates the key if it doesn't exist
     */
    fun encryptWithDefaultKey(data: String): String? {
        val defaultAlias = "default_app_key"
        generateSecretKey(defaultAlias)
        return encryptData(data, defaultAlias)
    }

    /**
     * Convenience method to decrypt data with a default key
     */
    fun decryptWithDefaultKey(encryptedData: String): String? {
        val defaultAlias = "default_app_key"
        return decryptData(encryptedData, defaultAlias)
    }
}
