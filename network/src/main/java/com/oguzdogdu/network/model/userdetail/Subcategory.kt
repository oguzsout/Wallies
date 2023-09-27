package com.oguzdogdu.network.model.userdetail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Subcategory(
    val pretty_slug: String,
    val slug: String
):Parcelable