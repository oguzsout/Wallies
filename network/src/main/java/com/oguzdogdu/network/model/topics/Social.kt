package com.oguzdogdu.network.model.topics


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Social(
    @SerializedName("instagram_username")
    val instagramUsername: String?,
    @SerializedName("paypal_email")
    val paypalEmail: String?,
    @SerializedName("portfolio_url")
    val portfolioUrl: String?,
    @SerializedName("twitter_username")
    val twitterUsername: String?
):Parcelable