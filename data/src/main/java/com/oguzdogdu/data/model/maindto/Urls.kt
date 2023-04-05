package com.oguzdogdu.data.model.maindto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Urls(
    @SerializedName("full")
    val full: String?,
    @SerializedName("raw")
    val raw: String?,
    @SerializedName("regular")
    val regular: String?,
    @SerializedName("small")
    val small: String?,
    @SerializedName("thumb")
    val thumb: String?
) : Parcelable