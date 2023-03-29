package com.oguzdogdu.wallies.presentation.popular

import com.oguzdogdu.domain.PopularImage

data class PopularState(
    val isLoading: Boolean = false,
    val popular: List<PopularImage?> = emptyList(),
    val error: String = ""
)
