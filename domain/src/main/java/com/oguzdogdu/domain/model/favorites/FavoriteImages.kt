package com.oguzdogdu.domain.model.favorites

data class FavoriteImages(
    val id: String,
    val url: String?,
    val profileImage: String?,
    val name: String?,
    val portfolioUrl: String?,
    var isChecked: Boolean = false
)
