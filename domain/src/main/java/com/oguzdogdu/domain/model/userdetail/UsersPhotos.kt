package com.oguzdogdu.domain.model.userdetail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UsersPhotos(
    val id : String?,
    val url:String?
):Parcelable
