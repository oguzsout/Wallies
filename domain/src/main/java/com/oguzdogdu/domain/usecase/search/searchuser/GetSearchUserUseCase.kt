package com.oguzdogdu.domain.usecase.search.searchuser

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.search.SearchPhoto
import com.oguzdogdu.domain.model.search.searchuser.SearchUser
import kotlinx.coroutines.flow.Flow

interface GetSearchUserUseCase {
    suspend operator fun invoke(query: String?): Flow<PagingData<SearchUser>>
}