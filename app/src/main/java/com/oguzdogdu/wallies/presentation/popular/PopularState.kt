package com.oguzdogdu.wallies.presentation.popular

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.popular.PopularImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class PopularState(
    val isLoading: Boolean = false,
    val popular: PagingData<PopularImage> = PagingData.empty(),
    val error: String = ""
)
