package com.oguzdogdu.wallies.presentation.search

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.search.SearchPhoto

sealed class SearchPhotoState {
    data class ItemState(
        val search: PagingData<SearchPhoto> = PagingData.empty()
    ) : SearchPhotoState()
}
