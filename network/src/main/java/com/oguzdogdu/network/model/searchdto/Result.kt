package com.oguzdogdu.network.model.searchdto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.oguzdogdu.network.model.maindto.Link
import com.oguzdogdu.network.model.maindto.Urls
import com.oguzdogdu.network.model.maindto.User
import com.oguzdogdu.domain.model.search.SearchPhoto
import kotlinx.parcelize.Parcelize

@Parcelize
data class Result(
    @SerializedName("alt_description")
    val alt_description: String?,
    @SerializedName("color")
    val color: String?,
    @SerializedName("created_at")
    val created_at: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("likes")
    val likes: Int?,
    @SerializedName("links")
    val links: Link?,
    @SerializedName("promoted_at")
    val promoted_at: String?,
    @SerializedName("updated_at")
    val updated_at: String?,
    @SerializedName("urls")
    val urls: Urls?,
    @SerializedName("user")
    val user: User?,
) : Parcelable

fun Result.toDomainSearch() = SearchPhoto(
    id = id,
    url = urls?.regular
)