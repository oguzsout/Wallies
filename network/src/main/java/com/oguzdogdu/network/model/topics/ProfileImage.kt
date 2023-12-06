package com.oguzdogdu.network.model.topics


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileImage(
    @SerializedName("large")
    val large: String?,
    @SerializedName("medium")
    val medium: String?,
    @SerializedName("small")
    val small: String?
):Parcelable