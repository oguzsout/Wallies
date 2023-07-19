package com.oguzdogdu.wallies.presentation.profiledetail

import com.oguzdogdu.domain.model.userdetail.UserDetails
import com.oguzdogdu.domain.model.userdetail.UsersPhotos

data class ProfileDetailState(
    val loading: Boolean = false,
    val userDetails: UserDetails? = null,
    val usersPhotos: List<UsersPhotos> = emptyList(),
    val errorMessage: String? = null
)
