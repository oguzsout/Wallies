package com.oguzdogdu.domain.model.auth

data class User(
    val name: String? = null,
    val surname: String? = null,
    val email: String? = null,
    val image: String? = null,
    val favorites: List<HashMap<String,String>>? = emptyList()
)
