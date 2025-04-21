package com.example.ratest.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Green,
    onPrimary = DarkGreen,
    secondary = LightGreen,
    onSecondary = White,
    background = White,
    onBackground = DarkGreen,
    surface = White,
    onSurface = DarkGreen,
    primaryContainer = Blue,
    onPrimaryContainer = White
)

@Composable
fun RAtestTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}