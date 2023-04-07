package com.oguzdogdu.data.model.searchdto

import androidx.paging.PagingData
import androidx.paging.map
import com.oguzdogdu.domain.model.search.SearchPhoto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class Result(
    val alt_description: String?,
    val blur_hash: String?,
    val color: String?,
    val created_at: String?,
    val current_user_collections: List<Any>?,
    val description: String?,
    val height: Int?,
    val id: String?,
    val liked_by_user: Boolean?,
    val likes: Int?,
    val links: Links?,
    val promoted_at: String?,
    val sponsorship: Any?,
    val tags: List<Tag>?,
    val updated_at: String?,
    val urls: UrlsX?,
    val user: User?,
    val width: Int?
)
fun Result.toDomainSearch() = SearchPhoto(
    id = id,
    url = urls?.regular
)

fun Flow<PagingData<Result>>.toSearchDomain(): Flow<PagingData<SearchPhoto>> {
    return map { pagingData ->
        pagingData.map { search ->
            SearchPhoto(
               id = search.id,
                url = search.urls?.regular
            )
        }
    }
}