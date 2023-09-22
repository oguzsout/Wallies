package com.oguzdogdu.wallieshd.presentation.profiledetail

sealed class ProfileDetailEvent {
    data class FetchUserDetailInfos(val username: String?) : ProfileDetailEvent()
    data class FetchUserCollections(val username: String?) : ProfileDetailEvent()
}
