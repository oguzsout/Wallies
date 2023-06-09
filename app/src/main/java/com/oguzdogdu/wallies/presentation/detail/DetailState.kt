package com.oguzdogdu.wallies.presentation.detail

import com.oguzdogdu.domain.model.singlephoto.Photo

data class DetailState(
    val isLoading: Boolean = false,
    val detail: Photo? = null,
    val error: String = ""
)
