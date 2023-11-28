package com.oguzdogdu.domain.model.userdetail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserCollections(
    val id: String?,
    val title: String?,
    val photo: String?,
    val desc: String?,
    val likes: Int?
): Parcelable
