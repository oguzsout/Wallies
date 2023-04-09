package com.oguzdogdu.data.model.maindto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.oguzdogdu.domain.model.latest.LatestImage
import com.oguzdogdu.domain.model.popular.PopularImage
import com.oguzdogdu.domain.model.singlephoto.Photo
import com.oguzdogdu.wallpaper.data.model.Sponsorship

@kotlinx.parcelize.Parcelize
data class UnsplashResponseItem(
    @SerializedName("alt_description")
    val altDescription: String?,
    @SerializedName("blur_hash")
    val blurHash: String?,
    @SerializedName("color")
    val color: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("height")
    val height: Int?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("liked_by_user")
    val likedByUser: Boolean?,
    @SerializedName("likes")
    val likes: Int?,
    @SerializedName("links")
    val links: Link?,
    @SerializedName("promoted_at")
    val promotedAt: String?,
    @SerializedName("sponsorship")
    val sponsorship: Sponsorship?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("urls")
    val urls: Urls?,
    @SerializedName("user")
    val user: User?,
    @SerializedName("width")
    val width: Int?,
    @SerializedName("views")
    val views: Double?,
    @SerializedName("downloads")
    val downloads: Int?
) : Parcelable

fun UnsplashResponseItem.toDomainModelPopular() = PopularImage(id = id,url = urls?.regular)
fun UnsplashResponseItem.toDomainModelLatest() = LatestImage(id = id,url = urls?.regular)

fun UnsplashResponseItem.toDomainModelPhoto() = Photo(
    id = id,
    username = user?.username,
    portfolio = user?.portfolioUrl,
    profileimage = user?.profileImage?.medium,
    desc = altDescription,
    urls = urls?.full,
    views = views,
    downloads = downloads,
    likes = likes
)