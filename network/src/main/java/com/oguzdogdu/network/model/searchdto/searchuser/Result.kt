package com.oguzdogdu.network.model.searchdto.searchuser

import android.os.Parcelable
import com.oguzdogdu.domain.model.search.SearchPhoto
import com.oguzdogdu.domain.model.search.searchuser.SearchUser
import com.oguzdogdu.network.model.searchdto.Result
import kotlinx.parcelize.Parcelize

@Parcelize
data class Result(
    val accepted_tos: Boolean,
    val bio: String,
    val first_name: String,
    val followed_by_user: Boolean,
    val for_hire: Boolean,
    val id: String,
    val instagram_username: String,
    val last_name: String,
    val links: Links,
    val location: String,
    val name: String,
    val photos: List<Photo>,
    val portfolio_url: String,
    val profile_image: ProfileImage,
    val social: Social,
    val total_collections: Int,
    val total_likes: Int,
    val total_photos: Int,
    val total_promoted_photos: Int,
    val twitter_username: String,
    val updated_at: String,
    val username: String
) : Parcelable

fun com.oguzdogdu.network.model.searchdto.searchuser.Result.toSearchUser() = SearchUser(
    id = id, username = username, profileImage = profile_image.medium, name = name
)
