package com.oguzdogdu.network.model.collection

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.oguzdogdu.network.model.maindto.Link
import com.oguzdogdu.network.model.maindto.Photo
import com.oguzdogdu.network.model.maindto.User
import com.oguzdogdu.domain.model.collection.WallpaperCollections
@kotlinx.parcelize.Parcelize
data class CollectionResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("published_at")
    val published_at: String?,
    @SerializedName("updated_at")
    val updated_at: String?,
    @SerializedName("total_photos")
    val total_photos: Int,
    @SerializedName("cover_photo")
    val cover_photo: Photo?,
    @SerializedName("preview_photos")
    val preview_photos: List<Photo>?,
    @SerializedName("user")
    val user: User?,
    @SerializedName("links")
    val links: Link?
) : Parcelable

fun CollectionResponse.toCollectionDomain() =
    WallpaperCollections(
        id = id,
        title = title,
        photo = cover_photo?.urls?.regular,
        desc = description,
        likes = cover_photo?.likes
    )