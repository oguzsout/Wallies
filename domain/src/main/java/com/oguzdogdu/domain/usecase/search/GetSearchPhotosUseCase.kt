package com.oguzdogdu.domain.usecase.search

import androidx.paging.PagingData
import com.oguzdogdu.domain.model.search.SearchPhoto
import kotlinx.coroutines.flow.Flow

interface GetSearchPhotosUseCase {
    suspend operator fun invoke(query: String?, language: String?): Flow<PagingData<SearchPhoto>>
}