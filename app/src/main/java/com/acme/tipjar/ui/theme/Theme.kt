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
    onPrimaryContainer = Color(0xFF0A3B25),
    secondary = Color(0xFF56635B),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCDEFD9),
    onSecondaryContainer = Color(0xFF0A3B25),
    tertiary = Color(0xFF3B6470),
    onTertiary = Color.White,
    background = Color(0xFFF6F7F3),
    onBackground = Color(0xFF14181A),
    surface = Color(0xFFF6F7F3),
    onSurface = Color(0xFF14181A),
    surfaceVariant = Color(0xFFE8EBE4),
    onSurfaceVariant = Color(0xFF4A524D),
    surfaceTint = Color(0xFF1E7F55),
    outline = Color(0xFF79817B),
    outlineVariant = Color(0xFFC6CCC5),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF0F2EC),
    surfaceContainer = Color(0xFFEBEEE7),
    surfaceContainerHigh = Color(0xFFE5E8E1),
    surfaceContainerHighest = Color(0xFFDFE3DB),
    error = Color(0xFFB3261E),
    onError = Color.White,
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF6FD8A0),
    onPrimary = Color(0xFF003921),
    primaryContainer = Color(0xFF17432E),
    onPrimaryContainer = Color(0xFFCDEFD9),
    secondary = Color(0xFFA9B8AE),
    onSecondary = Color(0xFF15231B),
    secondaryContainer = Color(0xFF17432E),
    onSecondaryContainer = Color(0xFFCDEFD9),
    tertiary = Color(0xFFA2CDDA),
    onTertiary = Color(0xFF033640),
    background = Color(0xFF0F1412),
    onBackground = Color(0xFFE6EAE6),
    surface = Color(0xFF0F1412),
    onSurface = Color(0xFFE6EAE6),
    surfaceVariant = Color(0xFF1E2622),
    onSurfaceVariant = Color(0xFFB9C2BB),
    surfaceTint = Color(0xFF6FD8A0),
    outline = Color(0xFF838C86),
    outlineVariant = Color(0xFF3A433E),
    surfaceContainerLowest = Color(0xFF0A0E0C),
    surfaceContainerLow = Color(0xFF151B18),
    surfaceContainer = Color(0xFF19201C),
    surfaceContainerHigh = Color(0xFF1E2622),
    surfaceContainerHighest = Color(0xFF29312D),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
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
