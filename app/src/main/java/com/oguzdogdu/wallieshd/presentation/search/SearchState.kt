package com.oguzdogdu.wallieshd.presentation.search

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.search.SearchPhoto

sealed class SearchState {
    data class PhotoState(
        val searchPhoto: PagingData<SearchPhoto> = PagingData.empty()
    ) : SearchState()
    data class UserState(
        val searchUser: PagingData<SearchPhoto> = PagingData.empty()
    ) : SearchState()
}
