package com.oguzdogdu.network.model.searchdto

import android.os.Parcelable
import com.oguzdogdu.network.model.maindto.Link
import com.oguzdogdu.network.model.maindto.Urls
import com.oguzdogdu.network.model.maindto.User
import com.oguzdogdu.domain.model.search.SearchPhoto
import kotlinx.parcelize.Parcelize

@Parcelize
data class Result(
    val alt_description: String?,
    val blur_hash: String?,
    val color: String?,
    val created_at: String?,
    val description: String?,
    val height: Int?,
    val id: String?,
    val liked_by_user: Boolean?,
    val likes: Int?,
    val links: Link?,
    val promoted_at: String?,
    val updated_at: String?,
    val urls: Urls?,
    val user: User?,
    val width: Int?
):Parcelable

fun Result.toDomainSearch() = SearchPhoto(
    id = id,
    url = urls?.regular
)