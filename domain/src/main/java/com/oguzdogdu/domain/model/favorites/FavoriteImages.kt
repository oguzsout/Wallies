package com.oguzdogdu.domain.model.favorites

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoriteImages(
    val id: String,
    val url: String?,
    val profileImage: String?,
    val name: String?,
    val portfolioUrl: String?,
    var isChecked: Boolean = false
):Parcelable
