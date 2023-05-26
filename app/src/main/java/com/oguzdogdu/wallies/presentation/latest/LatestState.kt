package com.oguzdogdu.wallies.presentation.latest

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.latest.LatestImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class LatestState(
    val isLoading: Boolean = false,
    val latest: Flow<PagingData<LatestImage>> = emptyFlow(),
    val error: String = ""
)