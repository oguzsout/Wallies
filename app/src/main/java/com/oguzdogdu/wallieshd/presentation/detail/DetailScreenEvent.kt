package com.oguzdogdu.wallieshd.presentation.detail

import com.oguzdogdu.domain.model.singlephoto.Photo

sealed class DetailScreenEvent {
    data class GetPhotoDetails(val id: String?) : DetailScreenEvent()
    data class GetPhotoFromWhere(val id: String?) : DetailScreenEvent()
    data class AddFavorites(val photo: Photo?) : DetailScreenEvent()
    data class DeleteFavorites(val photo: Photo?) : DetailScreenEvent()
    data class SetLoginDialogState(val isShown: Boolean) : DetailScreenEvent()
    data object CheckUserAuth : DetailScreenEvent()
}
