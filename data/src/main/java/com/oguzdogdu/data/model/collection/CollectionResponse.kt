package com.oguzdogdu.data.model.collection

import androidx.paging.PagingData
import androidx.paging.map
import com.oguzdogdu.data.model.maindto.Photo
import com.oguzdogdu.data.model.maindto.User
import com.oguzdogdu.data.model.searchdto.Links
import com.oguzdogdu.data.model.searchdto.Tag
import com.oguzdogdu.domain.model.collection.WallpaperCollections
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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
    val tags: List<Tag>?,
    val cover_photo: Photo?,
    val preview_photos: List<Photo>?,
    val user: User?,
    val links: Links?
)

fun CollectionResponse.toCollectionDomain() =
    WallpaperCollections(id = id, title = title ,photo = cover_photo?.urls?.regular)

fun Flow<PagingData<CollectionResponse>>.toDomain() : Flow<PagingData<WallpaperCollections>> {
    return map { result ->
        result.map {
            WallpaperCollections(id = it.id, title = it.title , photo = it.cover_photo?.urls?.regular)
        }
    }
}