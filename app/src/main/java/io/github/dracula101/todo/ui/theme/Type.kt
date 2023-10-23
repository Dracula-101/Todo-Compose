package io.github.dracula101.todo.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.github.dracula101.todo.R

val NatoFont = FontFamily(Font(R.font.lato))

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = NatoFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = NatoFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    bodySmall = TextStyle(
        fontFamily = NatoFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    titleLarge = TextStyle(
        fontFamily = NatoFont,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    titleMedium = TextStyle(
        fontFamily = NatoFont,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    titleSmall = TextStyle(
        fontFamily = NatoFont,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = NatoFont,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = NatoFont,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = NatoFont,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    labelLarge = TextStyle(
        fontFamily = NatoFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    labelMedium = TextStyle(
        fontFamily = NatoFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    labelSmall = TextStyle(
        fontFamily = NatoFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    displayLarge = TextStyle(
        fontFamily = NatoFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    displayMedium = TextStyle(
        fontFamily = NatoFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    displaySmall = TextStyle(
        fontFamily = NatoFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
)