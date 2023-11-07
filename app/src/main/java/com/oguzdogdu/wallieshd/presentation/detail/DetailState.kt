package com.oguzdogdu.wallieshd.presentation.detail

import com.oguzdogdu.domain.model.singlephoto.Photo

sealed class DetailState {
    object Loading : DetailState()
    data class DetailError(val errorMessage: String?) : DetailState()
    data class DetailOfPhoto(val detail: Photo? = null) : DetailState()
    data class FavoriteStateOfPhoto(val favorite: Boolean) : DetailState()
    data class StateOfLoginDialog(val isShown: Boolean) : DetailState()
}
