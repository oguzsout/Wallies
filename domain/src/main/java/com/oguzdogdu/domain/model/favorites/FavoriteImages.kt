package com.oguzdogdu.domain.model.favorites

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoriteImages(
    val id: String? = null,
    val url: String? = null,
    val profileImage: String? = null,
    val name: String? = null,
    val portfolioUrl: String? = null,
    var isChecked: Boolean = false
):Parcelable
