package com.oguzdogdu.domain.model.singlephoto

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
    val likes: Int?
)
