package com.coreproc.kotlin.kotlinbase.extensions

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.coreproc.kotlin.kotlinbase.ui.base.BaseActivity

/**
 * Theme management extensions for handling light/dark mode
 */

/**
 * Set the app theme programmatically
 */
fun Context.setThemeMode(mode: ThemeMode) {
    when (mode) {
        ThemeMode.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        ThemeMode.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        ThemeMode.SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        ThemeMode.AUTO_BATTERY -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
    }
}

/**
 * Get the current theme mode
 */
fun Context.getCurrentThemeMode(): ThemeMode {
    return when (AppCompatDelegate.getDefaultNightMode()) {
        AppCompatDelegate.MODE_NIGHT_NO -> ThemeMode.LIGHT
        AppCompatDelegate.MODE_NIGHT_YES -> ThemeMode.DARK
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> ThemeMode.SYSTEM
        AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY -> ThemeMode.AUTO_BATTERY
        else -> ThemeMode.SYSTEM
    }
}

/**
 * Toggle between light and dark mode
 */
fun Context.toggleTheme(): ThemeMode {
    val newMode = if (isDarkModeEnabled()) ThemeMode.LIGHT else ThemeMode.DARK
    setThemeMode(newMode)
    return newMode
}

/**
 * Apply theme-aware configuration on configuration changes
 */
fun BaseActivity.onConfigurationChanged() {
    // Reconfigure system bars when theme changes
    configureSystemBarsForTheme()
}

enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM,
    AUTO_BATTERY
}
