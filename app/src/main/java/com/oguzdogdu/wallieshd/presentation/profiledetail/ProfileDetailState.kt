package com.oguzdogdu.wallieshd.presentation.profiledetail

import com.oguzdogdu.domain.model.userdetail.UserDetails

sealed class ProfileDetailState {
    object Loading : ProfileDetailState()
    data class UserDetailError(val errorMessage: String?) : ProfileDetailState()
    data class UserCollectionsError(val errorMessage: String?) : ProfileDetailState()
    data class UserInfos(val userDetails: UserDetails?) : ProfileDetailState()
}
