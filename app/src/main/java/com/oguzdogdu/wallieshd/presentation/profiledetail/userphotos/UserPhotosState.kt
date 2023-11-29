package com.oguzdogdu.wallieshd.presentation.profiledetail.userphotos

import com.oguzdogdu.domain.model.userdetail.UsersPhotos

sealed interface UserPhotosState {
    object Loading : UserPhotosState
    data class UserPhotosError(val errorMessage: String?) : UserPhotosState
    data class UserPhotos(val usersPhotos: List<UsersPhotos>?) : UserPhotosState
}
