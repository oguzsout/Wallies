package com.oguzdogdu.wallies.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun String.formatDate(
    outputFormat: String = DateFormats.OUTPUT_DOTTED_LAST_PAYMENT_DATE_FORMAT.value,
    inputFormat: String = DateFormats.INPUT_DATE_FORMAT.value,
): String {
    var d: Date? = null

    val inputDateFormat = SimpleDateFormat(inputFormat, Locale.ENGLISH)
    val outputDateFormat = SimpleDateFormat(outputFormat, Locale("en"))

    try {
        d = inputDateFormat.parse(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return if (d == null) {
        this
    } else {
        outputDateFormat.format(d)
    }
}

enum class DateFormats(val value: String) {
    INPUT_DATE_FORMAT("yyyy-MM-dd'T'HH:mm:ss"),
    OUTPUT_BASIC_DATE_FORMAT("MMMM yyyy"),
    OUTPUT_LAST_PAYMENT_DATE_FORMAT("dd MMMM yyyy"),
    OUTPUT_DOTTED_LAST_PAYMENT_DATE_FORMAT("dd.MM.yyyy")
}