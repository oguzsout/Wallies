package com.oguzdogdu.network.model.auth

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