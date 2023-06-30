package com.oguzdogdu.data.model.auth

import com.oguzdogdu.data.model.collection.CollectionResponse
import com.oguzdogdu.domain.model.collection.WallpaperCollections

data class User(
    val name: String,
    val surname: String,
    val email: String,
)

fun User.toUserDomain() =
    com.oguzdogdu.domain.model.auth.User(
        name = name,
        surname = surname,
        email = email
    )