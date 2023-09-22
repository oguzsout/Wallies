package com.oguzdogdu.wallieshd.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.oguzdogdu.wallieshd.R

val QuickSand = FontFamily(
    Font(R.font.googlesansmedium)
)
val AppName = FontFamily(
    Font(R.font.cabalw5j3)
)

val Typography = Typography(
    body1 = TextStyle(
        fontFamily = QuickSand,
        fontSize = 16.sp
    ),
    body2 = TextStyle(
        fontFamily = AppName,
        fontSize = 32.sp
    )
)
