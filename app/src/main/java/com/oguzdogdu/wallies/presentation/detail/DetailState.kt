package com.oguzdogdu.wallies.presentation.detail

import com.oguzdogdu.domain.model.latest.LatestImage
import com.oguzdogdu.domain.model.singlephoto.Photo

data class DetailState(
    val isLoading: Boolean = false,
    val latest: Photo? = null,
    val error: String = ""
)
