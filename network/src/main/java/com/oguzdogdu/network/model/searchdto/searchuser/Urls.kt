package com.oguzdogdu.network.model.searchdto.searchuser

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Urls(
    val full: String,
    val raw: String,
    val regular: String,
    val small: String,
    val small_s3: String,
    val thumb: String
):Parcelable