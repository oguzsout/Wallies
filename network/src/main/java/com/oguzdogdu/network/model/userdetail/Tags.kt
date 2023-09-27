package com.oguzdogdu.network.model.userdetail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tags(
    val aggregated: List<Aggregated>
):Parcelable