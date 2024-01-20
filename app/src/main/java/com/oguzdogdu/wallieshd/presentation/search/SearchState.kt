package com.oguzdogdu.wallieshd.presentation.search

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.search.SearchPhoto
import com.oguzdogdu.domain.model.search.searchuser.SearchUser

sealed class SearchState {
    data class PhotoState(
        val searchPhoto: PagingData<SearchPhoto> = PagingData.empty()
    ) : SearchState()
    data class UserState(
        val searchUser: PagingData<SearchUser> = PagingData.empty()
    ) : SearchState()
}
