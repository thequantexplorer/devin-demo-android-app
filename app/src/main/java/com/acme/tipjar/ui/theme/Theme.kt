package com.acme.tipjar.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF1E7F55),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFCDEFD9),
    onPrimaryContainer = Color(0xFF07331F),
    secondary = Color(0xFF55665C),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD5E8DB),
    onSecondaryContainer = Color(0xFF12291C),
    background = Color(0xFFF6F7F3),
    onBackground = Color(0xFF14181A),
    surface = Color(0xFFF6F7F3),
    onSurface = Color(0xFF14181A),
    surfaceVariant = Color(0xFFE8EBE4),
    onSurfaceVariant = Color(0xFF454D48),
    outline = Color(0xFF747D77),
    outlineVariant = Color(0xFFC6CCC5),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF6FD8A0),
    onPrimary = Color(0xFF003920),
    primaryContainer = Color(0xFF1C4A34),
    onPrimaryContainer = Color(0xFFCDEFD9),
    secondary = Color(0xFFB5CCBD),
    onSecondary = Color(0xFF20352A),
    secondaryContainer = Color(0xFF2B3D33),
    onSecondaryContainer = Color(0xFFD5E8DB),
    background = Color(0xFF0F1412),
    onBackground = Color(0xFFE6EAE6),
    surface = Color(0xFF0F1412),
    onSurface = Color(0xFFE6EAE6),
    surfaceVariant = Color(0xFF1E2622),
    onSurfaceVariant = Color(0xFFBAC3BC),
    outline = Color(0xFF89938C),
    outlineVariant = Color(0xFF3A443E),
)

@Composable
fun TipJarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}
