package com.oguzdogdu.domain.model.singlephoto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    val id: String?,
    val username: String?,
    val portfolio: String?,
    val profileimage:String?,
    val createdAt: String?,
    val desc:String?,
    val urls: String?,
    val views: Double?,
    val downloads: Int?,
    val unsplashProfile: String?,
    val likes: Int?,
    val bio: String?,
    val name: String?,
    val tag: List<String?>?,
    val location: String?,
    val rawQuality: String?,
    val highQuality: String?,
    val mediumQuality: String?,
    val lowQuality: String?
):Parcelable
