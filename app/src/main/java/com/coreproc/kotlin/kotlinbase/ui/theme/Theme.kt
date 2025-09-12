package com.coreproc.kotlin.kotlinbase.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Light theme colors - GitHub inspired
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF24292f),           // GitHub dark gray
    onPrimary = Color.White,
    primaryContainer = Color(0xFF656d76),   // GitHub medium gray
    onPrimaryContainer = Color.White,
    secondary = Color(0xFF586069),          // GitHub muted gray
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF6a737d), // GitHub light gray
    onSecondaryContainer = Color.White,
    tertiary = Color(0xFF0969da),           // GitHub blue accent
    onTertiary = Color.White,
    background = Color(0xFFffffff),         // Pure white
    onBackground = Color(0xFF24292f),       // GitHub dark text
    surface = Color(0xFFffffff),            // Pure white
    onSurface = Color(0xFF24292f),          // GitHub dark text
    surfaceVariant = Color(0xFFf6f8fa),     // GitHub light background
    onSurfaceVariant = Color(0xFF656d76),   // GitHub medium gray text
    outline = Color(0xFFd0d7de),            // GitHub border gray
    error = Color(0xFFcf222e),              // GitHub red
    onError = Color.White,
    errorContainer = Color(0xFFffdcd7),     // Light red background
    onErrorContainer = Color(0xFF86181d)    // Dark red text
)

// Dark theme colors - GitHub Dark inspired
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFf0f6fc),           // GitHub dark mode light text
    onPrimary = Color(0xFF0d1117),         // GitHub dark background
    primaryContainer = Color(0xFF21262d),   // GitHub dark container
    onPrimaryContainer = Color(0xFFf0f6fc),
    secondary = Color(0xFF8b949e),          // GitHub dark muted text
    onSecondary = Color(0xFF0d1117),
    secondaryContainer = Color(0xFF30363d), // GitHub dark surface
    onSecondaryContainer = Color(0xFFf0f6fc),
    tertiary = Color(0xFF58a6ff),           // GitHub dark blue
    onTertiary = Color(0xFF0d1117),
    background = Color(0xFF0d1117),         // GitHub dark background
    onBackground = Color(0xFFf0f6fc),       // GitHub dark light text
    surface = Color(0xFF0d1117),            // GitHub dark background
    onSurface = Color(0xFFf0f6fc),          // GitHub dark light text
    surfaceVariant = Color(0xFF21262d),     // GitHub dark container
    onSurfaceVariant = Color(0xFF8b949e),   // GitHub dark muted text
    outline = Color(0xFF30363d),            // GitHub dark border
    error = Color(0xFFf85149),              // GitHub dark red
    onError = Color(0xFF0d1117),
    errorContainer = Color(0xFF8e1519),     // Dark red container
    onErrorContainer = Color(0xFFffdcd7)    // Light red text
)

@Composable
fun KotlinBaseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}
