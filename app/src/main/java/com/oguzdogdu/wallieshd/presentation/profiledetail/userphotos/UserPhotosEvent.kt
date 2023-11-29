package com.oguzdogdu.wallieshd.presentation.profiledetail.userphotos

sealed interface UserPhotosEvent {
    data class FetchUserPhotos(val username: String?) : UserPhotosEvent
}
