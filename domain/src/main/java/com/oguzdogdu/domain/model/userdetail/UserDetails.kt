package com.oguzdogdu.domain.model.userdetail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDetails(
    val name: String?,
    val bio: String?,
    val profileImage: String?,
    val postCount: Int?,
    val followingCount: Int?,
    val followersCount: Int?,
    val portfolioUrl: String?,
    val location: String?,
    val username: String?,
    val totalPhotos: Int?,
    val totalCollections: Int?,
    val instagram: String?,
    val twitter: String?,
    val portfolio: String?
) : Parcelable
