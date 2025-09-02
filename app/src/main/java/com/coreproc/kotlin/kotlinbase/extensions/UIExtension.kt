package com.coreproc.kotlin.kotlinbase.extensions

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import com.coreproc.kotlin.kotlinbase.ui.base.BaseActivity
import kotlin.math.PI

fun Context.showShortToast( message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showLongToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun View.setVisible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

fun BaseActivity.hideKeyboard() {
    val view = this.currentFocus
    view?.let { v ->
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? android.view.inputmethod.InputMethodManager
        imm?.hideSoftInputFromWindow(v.windowToken, 0)
    }
}

fun BaseActivity.applyWindowInsets() {
    // Enable edge-to-edge display
    WindowCompat.setDecorFitsSystemWindows(window, false)

    // Configure status bar appearance based on current theme
    val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
    val isDarkMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

    // Set appropriate status bar appearance
    windowInsetsController.isAppearanceLightStatusBars = !isDarkMode
    windowInsetsController.isAppearanceLightNavigationBars = !isDarkMode

    // Apply window insets to the root view
    val rootView = findViewById<View>(android.R.id.content)
    ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        val statusBars = insets.getInsets(WindowInsetsCompat.Type.statusBars())
        val navigationInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
        val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
        val isKeyboardVisible = imeInsets.bottom > 0

        val bottomInsets = if (isKeyboardVisible) {
            imeInsets
        } else {
            navigationInsets
        }

        view.updatePadding(
            left = systemBars.left,
            top = statusBars.top,
            right = systemBars.right,
            bottom = bottomInsets.bottom
        )
        insets
    }
}

/**
 * Check if the app is currently in dark mode
 */
fun Context.isDarkModeEnabled(): Boolean {
    return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}

/**
 * Apply window insets with automatic theme-aware status bar handling
 */
fun BaseActivity.applyThemeAwareWindowInsets() {
    // Enable edge-to-edge display
    WindowCompat.setDecorFitsSystemWindows(window, false)

    // Configure status bar appearance based on current theme
    configureSystemBarsForTheme()

    // Apply window insets to the root view
    val rootView = findViewById<View>(android.R.id.content)
    ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

        view.updatePadding(
            left = systemBars.left,
            top = systemBars.top,
            right = systemBars.right,
            bottom = systemBars.bottom
        )
        insets
    }
}

/**
 * Configure system bars appearance based on current theme
 */
fun BaseActivity.configureSystemBarsForTheme() {
    val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
    val isDarkMode = isDarkModeEnabled()

    // In light mode: use dark text/icons
    // In dark mode: use light text/icons
    windowInsetsController.isAppearanceLightStatusBars = !isDarkMode
    windowInsetsController.isAppearanceLightNavigationBars = !isDarkMode
}

/**
 * Get system bar insets for manual handling
 */
fun View.getSystemBarInsets(): WindowInsetsCompat? {
    return ViewCompat.getRootWindowInsets(this)
}

