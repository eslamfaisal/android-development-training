package com.training.ecommerce.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val LightColorPalette = lightColorScheme(
    primary = PrimaryBlueColor, primaryContainer = NeutralLightColor, secondary = NeutralDarkColor
)

private val DarkColorPalette = darkColorScheme(
    primary = PrimaryBlueColor, primaryContainer = NeutralLightColor, secondary = NeutralDarkColor
)

@Composable
fun EcommerceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    MaterialTheme(
        colorScheme = colors,
        typography = ECommerceTypography,
        content = content
    )
}