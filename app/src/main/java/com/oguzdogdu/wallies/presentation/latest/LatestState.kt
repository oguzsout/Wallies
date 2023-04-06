package com.oguzdogdu.wallies.presentation.latest

import com.oguzdogdu.domain.model.latest.LatestImage

data class LatestState(
    val isLoading: Boolean = false,
    val latest: List<LatestImage?> = emptyList(),
    val error: String = ""
)