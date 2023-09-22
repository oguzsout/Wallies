package com.oguzdogdu.wallieshd.presentation.latest

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.latest.LatestImage

sealed class LatestState {
    data class ItemState(
        val latest: PagingData<LatestImage> = PagingData.empty()
    ) : LatestState()
}
