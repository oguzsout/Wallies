package com.oguzdogdu.wallies.util

import java.text.NumberFormat
import kotlin.math.ln
import kotlin.math.pow

fun Double.toPrettyString(): String {
    if (this < 1000) return "$this"
    val exp = (ln(this) / ln(1000.0)).toInt()
    return String.format("%.1f%c", this / 1000.0.pow(exp.toDouble()), "KMGTPE"[exp - 1])
}
fun Int.toPrettyString(): String {
    if (this < 1000) return "$this"
    val exp = (ln(this.toDouble()) / ln(1000.0)).toInt()
    return String.format("%.1f%c", this / 1000.0.pow(exp.toDouble()), "KMGTPE"[exp - 1])
}