package com.oguzdogdu.data.model.searchdto

import com.oguzdogdu.data.model.maindto.Link
import com.oguzdogdu.data.model.maindto.Urls
import com.oguzdogdu.data.model.maindto.User
import com.oguzdogdu.domain.model.search.SearchPhoto

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
    val links: Link?,
    val promoted_at: String?,
    val sponsorship: Any?,
    val updated_at: String?,
    val urls: Urls?,
    val user: User?,
    val width: Int?
)

fun Result.toDomainSearch() = SearchPhoto(
    id = id,
    url = urls?.regular
)