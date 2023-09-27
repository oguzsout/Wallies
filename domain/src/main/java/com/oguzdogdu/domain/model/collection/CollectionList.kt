package com.oguzdogdu.domain.model.collection

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CollectionList(
    val id: String?,
    val url: String?,
    val desc: String?
):Parcelable
