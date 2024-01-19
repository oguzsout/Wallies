package com.oguzdogdu.network.model.searchdto.searchuser

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchUsersResponse(
    val results: List<Result>,
    val total: Int,
    val total_pages: Int
): Parcelable