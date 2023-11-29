package com.oguzdogdu.wallieshd.presentation.profiledetail

sealed class ProfileDetailEvent {
    object FetchUserDetailInfos : ProfileDetailEvent()
    object FetchUserPhotosList : ProfileDetailEvent()
    object FetchUserCollectionsList : ProfileDetailEvent()
}
