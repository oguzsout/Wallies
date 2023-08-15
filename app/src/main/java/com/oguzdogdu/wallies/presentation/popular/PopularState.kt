package com.oguzdogdu.wallies.presentation.popular

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.popular.PopularImage

sealed class PopularState {
    data class ItemState(
        val popular: PagingData<PopularImage> = PagingData.empty()
    ) : PopularState()
}
