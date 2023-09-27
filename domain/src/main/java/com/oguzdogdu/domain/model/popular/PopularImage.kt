package com.oguzdogdu.domain.model.popular

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PopularImage(
    val id : String?,
    val url:String?
):Parcelable
