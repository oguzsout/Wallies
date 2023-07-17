package com.oguzdogdu.domain.model.userdetail

data class UserDetails(
    val name: String?,
    val bio: String?,
    val profileImage: String?,
    val postCount: Int?,
    val followingCount: Int?,
    val followersCount: Int?,
    val portfolioUrl:String?,
)
