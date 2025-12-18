package com.shoaib.notes_app_kmp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import notes_app_kmp.composeapp.generated.resources.Res
import notes_app_kmp.composeapp.generated.resources.nunito_black
import notes_app_kmp.composeapp.generated.resources.nunito_black_italic
import notes_app_kmp.composeapp.generated.resources.nunito_bold
import notes_app_kmp.composeapp.generated.resources.nunito_bold_italic
import notes_app_kmp.composeapp.generated.resources.nunito_extra_bold
import notes_app_kmp.composeapp.generated.resources.nunito_extra_bold_italic
import notes_app_kmp.composeapp.generated.resources.nunito_extra_light
import notes_app_kmp.composeapp.generated.resources.nunito_extra_light_italic
import notes_app_kmp.composeapp.generated.resources.nunito_italic
import notes_app_kmp.composeapp.generated.resources.nunito_light
import notes_app_kmp.composeapp.generated.resources.nunito_light_italic
import notes_app_kmp.composeapp.generated.resources.nunito_medium
import notes_app_kmp.composeapp.generated.resources.nunito_medium_italic
import notes_app_kmp.composeapp.generated.resources.nunito_regular
import notes_app_kmp.composeapp.generated.resources.nunito_semi_bold
import notes_app_kmp.composeapp.generated.resources.nunito_semi_bold_italic


@Composable
fun nunitoFontFamily(): FontFamily {
    val fonts = listOf(
        // Regular weights
        Font(Res.font.nunito_regular, FontWeight.Normal, FontStyle.Normal),
        Font(Res.font.nunito_italic, FontWeight.Normal, FontStyle.Italic),

        // Light weights
        Font(Res.font.nunito_light, FontWeight.Light, FontStyle.Normal),
        Font(Res.font.nunito_light_italic, FontWeight.Light, FontStyle.Italic),

        // Medium weights
        Font(Res.font.nunito_medium, FontWeight.Medium, FontStyle.Normal),
        Font(Res.font.nunito_medium_italic, FontWeight.Medium, FontStyle.Italic),

        // SemiBold weights
        Font(Res.font.nunito_semi_bold, FontWeight.SemiBold, FontStyle.Normal),
        Font(Res.font.nunito_semi_bold_italic, FontWeight.SemiBold, FontStyle.Italic),

        // Bold weights
        Font(Res.font.nunito_bold, FontWeight.Bold, FontStyle.Normal),
        Font(Res.font.nunito_bold_italic, FontWeight.Bold, FontStyle.Italic),

        // ExtraBold weights
        Font(Res.font.nunito_extra_bold, FontWeight.ExtraBold, FontStyle.Normal),
        Font(Res.font.nunito_extra_bold_italic, FontWeight.ExtraBold, FontStyle.Italic),

        // Black weights
        Font(Res.font.nunito_black, FontWeight.Black, FontStyle.Normal),
        Font(Res.font.nunito_black_italic, FontWeight.Black, FontStyle.Italic),

        // ExtraLight weights
        Font(Res.font.nunito_extra_light, FontWeight.ExtraLight, FontStyle.Normal),
        Font(Res.font.nunito_extra_light_italic, FontWeight.ExtraLight, FontStyle.Italic)
    )
    return FontFamily(*fonts.toTypedArray())
}

// Custom Typography using Nunito font
@Composable
fun appTypography(): Typography {
    val fontFamily = nunitoFontFamily()
    return Typography(
        displayLarge = TextStyle(
            fontFamily = fontFamily,
        fontSize = 57.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 64.sp
    ),
        displayMedium = TextStyle(
        fontFamily = fontFamily,
        fontSize = 45.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 52.sp
    ),
        displaySmall = TextStyle(
        fontFamily = fontFamily,
        fontSize = 36.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 44.sp
    ),
        headlineLarge = TextStyle(
        fontFamily = fontFamily,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 40.sp
    ),
        headlineMedium = TextStyle(
        fontFamily = fontFamily,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 36.sp
    ),
        headlineSmall = TextStyle(
        fontFamily = fontFamily,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 32.sp
    ),
        titleLarge = TextStyle(
        fontFamily = fontFamily,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = fontFamily,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 24.sp
    ),
    titleSmall = TextStyle(
        fontFamily = fontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 20.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = fontFamily,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = fontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = fontFamily,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = fontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontFamily = fontFamily,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = fontFamily,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 16.sp
    )
)
}