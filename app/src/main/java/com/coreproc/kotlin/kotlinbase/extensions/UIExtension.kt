package com.coreproc.kotlin.kotlinbase.extensions

import android.content.Context
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

    // Configure status bar appearance for light backgrounds
    val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
    windowInsetsController.isAppearanceLightStatusBars = true
    windowInsetsController.isAppearanceLightNavigationBars = true

    // Apply window insets to the root view
    val rootView = findViewById<View>(android.R.id.content)
    ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        val navigationBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
        val statusBars = insets.getInsets(WindowInsetsCompat.Type.statusBars())

        view.updatePadding(
            left = systemBars.left,
            top = statusBars.top,
            right = systemBars.right,
            bottom = navigationBars.bottom
        )
        insets
    }
}

/**
 * Apply window insets to a specific view with custom padding handling
 */
fun View.applySystemWindowInsets(
    applyLeft: Boolean = true,
    applyTop: Boolean = true,
    applyRight: Boolean = true,
    applyBottom: Boolean = true
) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

        view.updatePadding(
            left = if (applyLeft) systemBars.left else view.paddingLeft,
            top = if (applyTop) systemBars.top else view.paddingTop,
            right = if (applyRight) systemBars.right else view.paddingRight,
            bottom = if (applyBottom) systemBars.bottom else view.paddingBottom
        )
        insets
    }
}

/**
 * Apply window insets with margins instead of padding
 */
fun View.applySystemWindowInsetsAsMargin(
    applyLeft: Boolean = true,
    applyTop: Boolean = true,
    applyRight: Boolean = true,
    applyBottom: Boolean = true
) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

        view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            leftMargin = if (applyLeft) systemBars.left else leftMargin
            topMargin = if (applyTop) systemBars.top else topMargin
            rightMargin = if (applyRight) systemBars.right else rightMargin
            bottomMargin = if (applyBottom) systemBars.bottom else bottomMargin
        }
        insets
    }
}

/**
 * Get system bar insets for manual handling
 */
fun View.getSystemBarInsets(): WindowInsetsCompat? {
    return ViewCompat.getRootWindowInsets(this)
}

/**
 * Apply keyboard (IME) insets handling
 */
fun View.applyKeyboardInsets(callback: ((Boolean, Int) -> Unit)? = null) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
        val isKeyboardVisible = imeInsets.bottom > 0

        // Apply bottom padding for keyboard
        view.updatePadding(bottom = imeInsets.bottom)

        // Optional callback for custom handling
        callback?.invoke(isKeyboardVisible, imeInsets.bottom)

        insets
    }
}

/**
 * Handle gesture navigation insets (Android 10+ gesture navigation)
 */
fun View.applyGestureInsets() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
            val gestureInsets = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                insets.getInsets(WindowInsetsCompat.Type.systemGestures())
            } else {
                insets.getInsets(WindowInsetsCompat.Type.systemBars())
            }

            view.updatePadding(
                left = gestureInsets.left,
                right = gestureInsets.right
            )
            insets
        }
    }
}