package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = ElegantDarkPrimary,
    onPrimary = ElegantDarkOnPrimary,
    primaryContainer = ElegantDarkPrimaryContainer,
    onPrimaryContainer = ElegantDarkOnPrimaryContainer,
    secondary = ElegantDarkSecondary,
    onSecondary = ElegantDarkOnSecondary,
    secondaryContainer = ElegantDarkSecondaryContainer,
    onSecondaryContainer = ElegantDarkOnSecondaryContainer,
    tertiary = ElegantDarkTertiary,
    onTertiary = ElegantDarkOnTertiary,
    tertiaryContainer = ElegantDarkTertiaryContainer,
    onTertiaryContainer = ElegantDarkOnTertiaryContainer,
    background = ElegantDarkBackground,
    onBackground = ElegantDarkOnBackground,
    surface = ElegantDarkSurface,
    onSurface = ElegantDarkOnSurface,
    surfaceVariant = ElegantDarkSurfaceVariant,
    onSurfaceVariant = ElegantDarkOnSurfaceVariant,
    outline = ElegantDarkOutline,
    outlineVariant = ElegantDarkOutlineVariant
)

private val LightColorScheme = lightColorScheme(
    primary = QessaLightPrimary,
    onPrimary = QessaLightOnPrimary,
    primaryContainer = QessaLightPrimaryContainer,
    onPrimaryContainer = QessaLightOnPrimaryContainer,
    secondary = QessaLightSecondary,
    onSecondary = QessaLightOnSecondary,
    tertiary = QessaLightTertiary,
    onTertiary = QessaLightOnSecondary,
    background = QessaLightBackground,
    onBackground = QessaLightOnBackground,
    surface = QessaLightSurface,
    onSurface = QessaLightOnSurface,
    surfaceVariant = QessaLightSurfaceVariant,
    onSurfaceVariant = QessaLightOnSurfaceVariant,
    outline = QessaLightBorder,
    outlineVariant = ElegantDarkOutlineVariant
)

@Composable
fun QessaTheme(
    darkTheme: Boolean = true, // Default to Elegant Dark
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    QessaTheme(darkTheme = darkTheme, content = content)
}
