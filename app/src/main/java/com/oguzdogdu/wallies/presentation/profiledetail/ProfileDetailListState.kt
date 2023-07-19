package com.oguzdogdu.wallies.presentation.profiledetail

import com.oguzdogdu.domain.model.userdetail.UsersPhotos

data class ProfileDetailListState(
    val loading: Boolean = false,
    val usersPhotos: List<UsersPhotos> = emptyList(),
    val errorMessage: String? = null
)
