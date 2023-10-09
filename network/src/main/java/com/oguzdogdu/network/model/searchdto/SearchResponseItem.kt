package com.oguzdogdu.network.model.searchdto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchResponseItem(
    @SerializedName("results")
    val results: List<Result>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("total_pages")
    val total_pages: Int
):Parcelable