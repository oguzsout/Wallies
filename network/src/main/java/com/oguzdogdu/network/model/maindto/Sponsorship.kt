package com.oguzdogdu.network.model.maindto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.oguzdogdu.network.model.maindto.Sponsor
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