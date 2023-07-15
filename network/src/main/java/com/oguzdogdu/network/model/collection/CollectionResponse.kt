package com.oguzdogdu.network.model.collection

import com.oguzdogdu.network.model.maindto.Link
import com.oguzdogdu.network.model.maindto.Photo
import com.oguzdogdu.network.model.maindto.User
import com.oguzdogdu.domain.model.collection.WallpaperCollections

data class CollectionResponse(
    val id: String?,
    val title: String?,
    val description: String?,
    val published_at: String?,
    val updated_at: String?,
    val curated: Boolean?,
    val featured: Boolean?,
    val total_photos: Int,
    val private: Boolean?,
    val share_key: String?,
    val cover_photo: Photo?,
    val preview_photos: List<Photo>?,
    val user: User?,
    val links: Link?
)

fun CollectionResponse.toCollectionDomain() =
    WallpaperCollections(
        id = id,
        title = title,
        photo = cover_photo?.urls?.regular,
        desc = description
    )