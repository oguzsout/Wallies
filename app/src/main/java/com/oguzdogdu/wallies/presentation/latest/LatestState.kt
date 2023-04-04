package com.oguzdogdu.wallies.presentation.latest

import com.oguzdogdu.domain.LatestImage
import com.oguzdogdu.domain.PopularImage

data class LatestState(
    val isLoading: Boolean = false,
    val latest: List<LatestImage?> = emptyList(),
    val error: String = ""
)