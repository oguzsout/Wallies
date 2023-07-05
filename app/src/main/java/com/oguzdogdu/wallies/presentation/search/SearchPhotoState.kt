package com.oguzdogdu.wallies.presentation.search

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.search.SearchPhoto

data class SearchPhotoState(
    val isLoading: Boolean = false,
    val query: String? = null,
    val search: PagingData<SearchPhoto> = PagingData.empty(),
    val error: String = ""
)
