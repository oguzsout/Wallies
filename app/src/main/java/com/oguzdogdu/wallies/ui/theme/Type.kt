package com.oguzdogdu.wallies.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.sp
import com.oguzdogdu.wallies.R



val QuickSand = FontFamily(
    Font(R.font.googlesansmedium),
)

val Typography = Typography(
    body1 = TextStyle(
        fontFamily = QuickSand,
        fontSize = 16.sp,
    )
)