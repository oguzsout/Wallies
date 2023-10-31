package com.oguzdogdu.domain.model.collection

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WallpaperCollections(
    val id: String?,
    val title: String?,
    val photo: String?,
    val desc: String?,
    val likes: Int?
):Parcelable
