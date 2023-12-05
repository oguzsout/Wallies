package com.oguzdogdu.network.model.topics


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @SerializedName("accepted_tos")
    val acceptedTos: Boolean?,
    @SerializedName("bio")
    val bio: String?,
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("for_hire")
    val forHire: Boolean?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("instagram_username")
    val instagramUsername: String?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("links")
    val links: Links?,
    @SerializedName("location")
    val location: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("portfolio_url")
    val portfolioUrl: String?,
    @SerializedName("profile_image")
    val profileİmage: ProfileImage?,
    @SerializedName("social")
    val social: Social?,
    @SerializedName("total_collections")
    val totalCollections: Int?,
    @SerializedName("total_likes")
    val totalLikes: Int?,
    @SerializedName("total_photos")
    val totalPhotos: Int?,
    @SerializedName("total_promoted_photos")
    val totalPromotedPhotos: Int?,
    @SerializedName("twitter_username")
    val twitterUsername: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("username")
    val username: String?
):Parcelable