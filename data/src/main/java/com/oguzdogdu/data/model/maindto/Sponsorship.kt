package com.oguzdogdu.wallpaper.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Sponsorship(
    @SerializedName("sponsor")
    val sponsor: Sponsor?,
    @SerializedName("tagline")
    val tagline: String?,
    @SerializedName("tagline_url")
    val taglineUrl: String?
) : Parcelable