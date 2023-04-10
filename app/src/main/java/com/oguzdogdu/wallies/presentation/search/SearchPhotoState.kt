package com.oguzdogdu.wallies.presentation.search

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.search.SearchPhoto
import com.oguzdogdu.domain.model.singlephoto.Photo

data class SearchPhotoState(
    val isLoading: Boolean = false,
    val search: PagingData<SearchPhoto> = PagingData.empty(),
    val error: String = ""
)
