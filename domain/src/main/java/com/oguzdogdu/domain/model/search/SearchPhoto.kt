package com.oguzdogdu.domain.model.search

import android.os.Parcelable
import androidx.annotation.IdRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchPhoto(
    val id: String?,
    val url: String?
):Parcelable
