package com.example.teachflow.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Light Theme Colors
val LightPrimary = Color(0xFF2196F3)
val LightPrimaryDark = Color(0xFF1976D2)
val LightPrimaryLight = Color(0xFF64B5F6)
val LightAccent = Color(0xFF00BCD4)
val LightBackground = Color(0xFFF5F7FA)
val LightSurface = Color(0xFFFFFFFF)
val LightTextPrimary = Color(0xFF1A1A2E)
val LightTextSecondary = Color(0xFF666666)
val LightTextHint = Color(0xFF999999)

// Dark Theme Colors
val DarkPrimary = Color(0xFF64B5F6)
val DarkPrimaryDark = Color(0xFF2196F3)
val DarkPrimaryLight = Color(0xFF90CAF9)
val DarkAccent = Color(0xFF80DEEA)
val DarkBackground = Color(0xFF121212)
val DarkSurface = Color(0xFF1E1E1E)
val DarkTextPrimary = Color(0xFFFFFFFF)
val DarkTextSecondary = Color(0xFFB0B0B0)
val DarkTextHint = Color(0xFF757575)

@Composable
fun TeachFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColorScheme(
            primary = DarkPrimary,
            secondary = DarkAccent,
            background = DarkBackground,
            surface = DarkSurface,
            onPrimary = Color.White,
            onSecondary = Color.White,
            onBackground = DarkTextPrimary,
            onSurface = DarkTextPrimary
        )
    } else {
        lightColorScheme(
            primary = LightPrimary,
            secondary = LightAccent,
            background = LightBackground,
            surface = LightSurface,
            onPrimary = Color.White,
            onSecondary = Color.White,
            onBackground = LightTextPrimary,
            onSurface = LightTextPrimary
        )
    }
    
    MaterialTheme(
        colorScheme = colors,
        typography = Typography(),
        content = content
    )
}
