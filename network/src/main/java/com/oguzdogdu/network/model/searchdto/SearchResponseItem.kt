package com.oguzdogdu.network.model.searchdto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchResponseItem(
    val results: List<Result>,
    val total: Int,
    val total_pages: Int
):Parcelable