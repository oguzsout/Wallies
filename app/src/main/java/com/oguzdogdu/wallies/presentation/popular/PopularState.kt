package com.oguzdogdu.wallies.presentation.popular

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.popular.PopularImage

data class PopularState(
    val isLoading: Boolean = false,
    val popular: PagingData<PopularImage> = PagingData.empty(),
    val error: String = ""
)
