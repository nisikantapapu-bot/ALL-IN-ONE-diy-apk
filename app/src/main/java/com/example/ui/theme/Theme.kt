package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = SaffronPrimary,
    onPrimary = Color.White,
    primaryContainer = SaffronContainer,
    onPrimaryContainer = OnSaffronContainer,
    secondary = FreshGreen,
    onSecondary = Color.White,
    secondaryContainer = FreshGreenContainer,
    onSecondaryContainer = OnFreshGreenContainer,
    tertiary = CobaltBlue,
    onTertiary = Color.White,
    tertiaryContainer = CobaltBlueContainer,
    background = BackgroundWarm,
    onBackground = TextPrimaryDark,
    surface = SurfaceWarm,
    onSurface = TextPrimaryDark,
    surfaceVariant = SurfaceVariantWarm,
    onSurfaceVariant = TextSecondaryMuted,
    outline = BorderSubtle
)

private val DarkColorScheme = darkColorScheme(
    primary = SaffronLight,
    onPrimary = Color(0xFF451900),
    primaryContainer = SaffronDark,
    onPrimaryContainer = Color(0xFFFFDBCF),
    secondary = Color(0xFF81C784),
    onSecondary = Color(0xFF00390E),
    secondaryContainer = Color(0xFF1B5E20),
    onSecondaryContainer = Color(0xFFA5D6A7),
    tertiary = Color(0xFF90CAF9),
    background = Color(0xFF181512),
    onBackground = Color(0xFFEDE0D4),
    surface = Color(0xFF221F1B),
    onSurface = Color(0xFFEDE0D4),
    surfaceVariant = Color(0xFF332E28),
    onSurfaceVariant = Color(0xFFD2C5B9),
    outline = Color(0xFF4E473F)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep branded warmth
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
