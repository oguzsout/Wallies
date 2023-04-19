package com.oguzdogdu.wallies.presentation.latest

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.latest.LatestImage

data class LatestState(
    val isLoading: Boolean = false,
    val latest: PagingData<LatestImage> = PagingData.empty(),
    val error: String = ""
)