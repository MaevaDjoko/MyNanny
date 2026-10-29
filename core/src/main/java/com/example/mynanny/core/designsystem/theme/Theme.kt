package com.example.mynanny.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Raspberry40,
    onPrimary = Cream99,
    primaryContainer = Raspberry90,
    onPrimaryContainer = Raspberry10,
    secondary = Teal40,
    onSecondary = Cream99,
    secondaryContainer = Teal90,
    onSecondaryContainer = Teal20,
    background = Cream99,
    onBackground = Ink10,
    surface = Cream99,
    onSurface = Ink10,
    surfaceVariant = Cream95,
    onSurfaceVariant = Ink30,
)

private val DarkColorScheme = darkColorScheme(
    primary = Raspberry80,
    onPrimary = Raspberry20,
    primaryContainer = Raspberry40,
    onPrimaryContainer = Raspberry90,
    secondary = Teal80,
    onSecondary = Teal20,
    secondaryContainer = Teal40,
    onSecondaryContainer = Teal90,
    background = Night6,
    onBackground = Night90,
    surface = Night6,
    onSurface = Night90,
    surfaceVariant = Night12,
    onSurfaceVariant = Night80,
)

@Composable
fun MyNannyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}
