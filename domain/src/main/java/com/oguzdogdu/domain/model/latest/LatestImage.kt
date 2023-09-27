package com.oguzdogdu.domain.model.latest

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LatestImage(
    val id: String?,
    val url: String?
):Parcelable
