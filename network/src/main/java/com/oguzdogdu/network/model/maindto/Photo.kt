package com.oguzdogdu.network.model.maindto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.oguzdogdu.domain.model.collection.CollectionList
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    @SerializedName("id")
    val id: String?,
    @SerializedName("created_at")
    val created_at: String?,
    @SerializedName("updated_at")
    val updated_at: String?,
    @SerializedName("width")
    val width: Int?,
    @SerializedName("height")
    val height: Int?,
    @SerializedName("color")
    val color: String? = "#E0E0E0",
    @SerializedName("blur_hash")
    val blur_hash: String?,
    @SerializedName("views")
    val views: Int?,
    @SerializedName("downloads")
    val downloads: Int?,
    @SerializedName("likes")
    val likes: Int?,
    @SerializedName("liked_by_user")
    var liked_by_user: Boolean?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("alt_description")
    val alt_description: String?,
    @SerializedName("sponsorship")
    val sponsorship: Sponsorship?,
    @SerializedName("urls")
    val urls: Urls?,
    @SerializedName("links")
    val links: Link?,
    @SerializedName("user")
    val user: User?,
) : Parcelable

fun Photo.toDomain() = CollectionList(id = id, url = urls?.regular,desc = description)
