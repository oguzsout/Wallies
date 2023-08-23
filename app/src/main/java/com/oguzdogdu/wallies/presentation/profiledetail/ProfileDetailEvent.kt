package com.oguzdogdu.wallies.presentation.profiledetail

sealed class ProfileDetailEvent {
    data class FetchUserDetailInfos(val username: String?) : ProfileDetailEvent()
    data class FetchUserCollections(val username: String?) : ProfileDetailEvent()
}
