package com.upb.libreria.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Paleta inspirada en una librería clásica: cuero, papel y dorado.
val Cuero = Color(0xFF5D2E1F)
val CueroClaro = Color(0xFF8B4A33)
val Papel = Color(0xFFFFF8F0)
val PapelOscuro = Color(0xFFF3E6D8)
val Dorado = Color(0xFFC8902E)
val VerdeStock = Color(0xFF2E7D32)
val NaranjaStock = Color(0xFFE65100)
val RojoStock = Color(0xFFC62828)

private val EsquemaClaro = lightColorScheme(
    primary = Cuero,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDBCF),
    onPrimaryContainer = Color(0xFF3A0B00),
    secondary = Dorado,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFE0B2),
    onSecondaryContainer = Color(0xFF3E2700),
    background = Papel,
    onBackground = Color(0xFF231A16),
    surface = Papel,
    onSurface = Color(0xFF231A16),
    surfaceVariant = PapelOscuro,
    onSurfaceVariant = Color(0xFF53433E),
    surfaceContainer = Color(0xFFF8ECE2),
)

private val EsquemaOscuro = darkColorScheme(
    primary = Color(0xFFFFB59D),
    onPrimary = Color(0xFF5D1900),
    primaryContainer = CueroClaro,
    onPrimaryContainer = Color(0xFFFFDBCF),
    secondary = Color(0xFFFFB951),
    onSecondary = Color(0xFF452B00),
    secondaryContainer = Color(0xFF633F00),
    onSecondaryContainer = Color(0xFFFFDDB3),
    background = Color(0xFF1A110E),
    onBackground = Color(0xFFF1DFD9),
    surface = Color(0xFF1A110E),
    onSurface = Color(0xFFF1DFD9),
    surfaceVariant = Color(0xFF53433E),
    onSurfaceVariant = Color(0xFFD8C2BB),
)

private val Tipografia = Typography(
    titleLarge = TextStyle(fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 22.sp),
    titleMedium = TextStyle(fontFamily = FontFamily.Serif, fontWeight = FontWeight.SemiBold, fontSize = 17.sp),
    bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 20.sp),
    labelSmall = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Medium),
)

@Composable
fun LibreriaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) EsquemaOscuro else EsquemaClaro,
        typography = Tipografia,
        content = content
    )
}
