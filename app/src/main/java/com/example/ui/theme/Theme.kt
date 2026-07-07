package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = Cyan500, 
    secondary = Cyan400, 
    tertiary = Emerald400,
    background = AppBackground,
    surface = FrostedBackground,
    surfaceVariant = SysBorderInner,
    onPrimary = AppBackground,
    onSecondary = TextPrimary,
    onTertiary = AppBackground,
    onBackground = TextPrimary,
    onSurface = TextPrimary
  )

@Composable
fun MyApplicationTheme(
  // Force dark theme for anime aesthetic
  darkTheme: Boolean = true,
  // Disable dynamic color to maintain neon style
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = DarkColorScheme
  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
