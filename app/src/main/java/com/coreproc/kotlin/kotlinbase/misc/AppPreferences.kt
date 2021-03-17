package com.coreproc.kotlin.kotlinbase.misc

import android.content.Context
import com.coreproc.kotlin.kotlinbase.App

object AppPreferences {
    private val API_KEY = "API_KEY"

    private fun logout() {
        val prefs = App.instance!!.getSharedPreferences(App.instance!!.packageName, Context.MODE_PRIVATE)
        prefs.edit().putString(API_KEY, "").apply()
    }

    fun saveApiKey(key: String) {
        val prefs = App.instance!!.getSharedPreferences(App.instance!!.packageName, Context.MODE_PRIVATE)
        prefs.edit().putString(API_KEY, key).apply()
    }

    fun getApiKey(): String? {
        return try {
            val prefs = App.instance!!.getSharedPreferences(App.instance!!.packageName, Context.MODE_PRIVATE)
            return prefs.getString(API_KEY, "")
        } catch (ex: Exception) {
            null
        }
    }
}