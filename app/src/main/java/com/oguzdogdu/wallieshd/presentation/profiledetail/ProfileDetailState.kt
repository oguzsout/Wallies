package com.oguzdogdu.wallieshd.presentation.profiledetail

import com.oguzdogdu.domain.model.userdetail.UserDetails
import com.oguzdogdu.domain.model.userdetail.UsersPhotos

sealed class ProfileDetailState {
    object Loading : ProfileDetailState()
    data class UserDetailError(val errorMessage: String?) : ProfileDetailState()
    data class UserCollectionsError(val errorMessage: String?) : ProfileDetailState()
    data class UserCollections(val usersPhotos: List<UsersPhotos>?) : ProfileDetailState()
    data class UserInfos(val userDetails: UserDetails?) : ProfileDetailState()
}
