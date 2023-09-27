package com.oguzdogdu.network.model.userdetail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Aggregated(
    val source: Source,
    val title: String,
    val type: String
):Parcelable